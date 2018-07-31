package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.developer.controller.request.DeliveryOutExeclRequest;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.controller.request.SDeliveryUpdateRequest;
import org.tis.senior.module.developer.dao.SDeliveryMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.*;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.tmatesoft.svn.core.SVNException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sDelivery的Service接口实现类
 *
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SDeliveryServiceImpl extends ServiceImpl<SDeliveryMapper, SDelivery> implements ISDeliveryService {

    @Autowired
    private ISBranchMappingService branchMappingService;
    @Autowired
    private ISBranchService branchService;
    @Autowired
    private ISWorkitemService workitemService;
    @Autowired
    private ISDeliveryListService deliveryListService;
    @Autowired
    private ISProjectService projectService;
    @Autowired
    private ISCheckService checkService;
    @Autowired
    private ISProfilesService profilesService;

    @Override
    public Page<SDelivery> getDeliveryAll(Page<SDelivery> page, EntityWrapper<SDelivery> wrapper, SSvnAccount svnAccount) {
        if(!svnAccount.getRole().equals(SvnRole.RCT)){
            wrapper.like(SDelivery.COLUMN_PROPOSER, svnAccount.getUserId());
        }
        return page.setRecords(this.baseMapper.selectPage(page, wrapper));
    }

    @Override
    public DeliveryDetail getMergeInfo(MergeDeliveryRequest mergeDelivery, String userId) {
        List<SDelivery> deliveryList = isAllowMerge(mergeDelivery.getMergeList());
        // 获取对应工作项信息
        List<String> workitemGuids = deliveryList.stream()
                .map(SDelivery::getGuidWorkitem).map(String::valueOf).collect(Collectors.toList());
        EntityWrapper<SWorkitem> workitemWrapper = new EntityWrapper<>();
        workitemWrapper.in(SWorkitem.COLUMN_GUID, workitemGuids);
        List<SWorkitem> sWorkitems = workitemService.selectList(workitemWrapper);
        // 获取分支信息
        EntityWrapper<SBranchMapping> branchMappingWrapper = new EntityWrapper<>();
        branchMappingWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM.getValue());
        branchMappingWrapper.in(SBranchMapping.COLUMN_GUID_OF_WHATS, workitemGuids);
        List<String> branchGuids = branchMappingService.selectList(branchMappingWrapper).stream()
                .map(SBranchMapping::getGuidBranch).map(Object::toString).collect(Collectors.toList());
        EntityWrapper<SBranch> branchWrapper = new EntityWrapper<>();
        branchWrapper.in(SBranch.COLUMN_GUID, branchGuids);
        List<SBranch> sBranches = branchService.selectList(branchWrapper);

        // 获取清单详情
        EntityWrapper<SDeliveryList> deliveryListWrapper = new EntityWrapper<>();
        deliveryListWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, mergeDelivery.getMergeList());
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(deliveryListWrapper);
        List<DeliveryProjectDetail> details = DeliveryProjectDetail.getDeliveryDetail(sDeliveryLists);
        // 统计投放消息
        // 补丁类型
        Map<String, Integer> patchCount = new HashMap<>(5);
        details.forEach(d -> d.getPatchType().forEach(p -> {
            patchCount.merge(p, 1, (a, b) -> a + b);
        }));

        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setWorkitems(sWorkitems);
        deliveryDetail.setBranches(sBranches);
        deliveryDetail.setDetailList(details);
        deliveryDetail.setPatchCount(patchCount);

        return deliveryDetail;
    }

    @Override
    public void mergeDelivery(MergeDeliveryRequest mergeDelivery, String userId) {
        List<SDelivery> deliveryList = isAllowMerge(mergeDelivery.getMergeList());
        EntityWrapper<SDeliveryList> wrapper = new EntityWrapper<>();
        wrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, mergeDelivery.getMergeList());
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(wrapper);
        // 合并为一个投产申请,每个环境形成一个独立的投放申请
        mergeDelivery.getProfiles().forEach(p -> {
            SDelivery sDelivery = new SDelivery();
            sDelivery.setGuidProfiles(p.getGuidProfiles());
            sDelivery.setPackTiming(p.getPackTiming());
            sDelivery.setDeliveryType(DeliveryType.MERGE);
            sDelivery.setMergeList(mergeDelivery.getMergeList().stream().reduce("", (r, s) -> r + "," + s));
            String appAlias = deliveryList.stream()
                    .map(SDelivery::getApplyAlias).reduce("合并投放:", (r, s) -> r + s + "，");
            sDelivery.setApplyAlias(appAlias.substring(0, appAlias.length() - 1));
            sDelivery.setApplyTime(new Date());
            sDelivery.setProposer(userId);
            sDelivery.setDeliveryResult(DeliveryResult.APPLYING);
            insert(sDelivery);
            sDeliveryLists.forEach(s -> s.setGuidDelivery(sDelivery.getGuid()));
            deliveryListService.insertBatch(sDeliveryLists);
        });
    }

    @Override
    public void merge(String id) {
        SDelivery delivery = this.baseMapper.selectById(id);
        if (delivery == null) {
            throw new DeveloperException("找不到" + id + "对应投放申请");
        }
        if (!delivery.getDeliveryResult().equals(DeliveryResult.APPLYING)) {
            throw new DeveloperException("投放申请'" + delivery.getApplyAlias() + "'当前状态为" +
                    delivery.getDeliveryResult().toString() + "，只能合并申请中状态的投放申请！");
        }
        delivery.setDeliveryResult(DeliveryResult.MERGED);
        this.baseMapper.updateById(delivery);
    }

    /**
     * 判断是否允许合并投放，返回合并申请信息集合
     *
     * @param deliveryGuids
     * @return
     * @throws DeveloperException
     */
    private List<SDelivery> isAllowMerge(List<String> deliveryGuids) throws DeveloperException {
        // 查询合并清单信息
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.in(SDelivery.COLUMN_GUID, deliveryGuids);
        List<SDelivery> sDeliveries = this.baseMapper.selectList(deliveryWrapper);
        if (CollectionUtils.isEmpty(sDeliveries)) {
            throw new DeveloperException("合并的投放申请不存在！");
        }
        // 统计
        long count = sDeliveries.stream()
                .filter(d -> !d.getDeliveryResult().isSuccess()).count();
        if (count > 0) {
            throw new DeveloperException("合并的投放申请状态必须为成功！");
        }
        long profileCount = sDeliveries.stream().map(SDelivery::getGuidProfiles).distinct().count();
        if (profileCount > 1) {
            throw new DeveloperException("合并的投放申请环境必须相同！");
        }
        return sDeliveries;
    }

    @Override
    public List<String> selectDeliveryProName(String guidDelivery) {
        List<String> projectNameList = new ArrayList<>();
        SDelivery delivery = selectById(guidDelivery);

        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY, delivery.getGuid());
        List<SDeliveryList> deliveryLists = deliveryListService.selectList(deliveryListEntityWrapper);
        Set<String> str = new HashSet<>();
        for (SDeliveryList deliveryList : deliveryLists) {
            str.add(deliveryList.getPartOfProject());
        }
        projectNameList.addAll(str);
        return projectNameList;
    }


    @Override
    public List<SDelivery> selectAddToDelivery(Integer workitemGuid) {
        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM, workitemGuid);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT, DeliveryResult.APPLYING);
        List<SDelivery> deliveryList = selectList(deliveryEntityWrapper);
        if (deliveryList == null) {
            throw new DeveloperException("没有对应的投放申请可以追加！");
        }
        return deliveryList;
    }

    @Override
    public void deleteDeliveryAndDeliveryList(Integer guidDelivery) throws SVNException {

        SDelivery delivery = selectById(guidDelivery);
        if (delivery == null) {
            throw new DeveloperException("没有找到对应的投放申请！");
        }

        if (delivery.getDeliveryResult().equals(DeliveryResult.DELIVERED)) {
            throw new DeveloperException("此投放申请已成功投放，不允许删除！");
        }

        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY, delivery.getGuid());
        List<SDeliveryList> deliveryLists = deliveryListService.selectList(deliveryListEntityWrapper);

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.ne(SDelivery.COLUMN_GUID, delivery.getGuid());
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM, delivery.getGuidWorkitem());
        deliveryEntityWrapper.in(SDelivery.COLUMN_DELIVERY_RESULT, DeliveryResult.unfinished());
        List<SDelivery> deliveryList = selectList(deliveryEntityWrapper);

        if (deliveryList.size() < 1) {
            SWorkitem workitem = workitemService.selectById(delivery.getGuidWorkitem());

            EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
            branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, workitem.getGuid());
            branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM);
            List<SBranchMapping> branchMapping = branchMappingService.selectList(branchMappingEntityWrapper);

            if (branchMapping.size() > 0) {
                branchService.revertBranchRevision(branchMapping.get(0).getGuidBranch());

            }

        }
        if (deliveryLists.size() > 0) {
            deliveryListService.deleteBatchIds(deliveryLists.stream().map(SDeliveryList::getGuid).collect(Collectors.toList()));
        }
        deleteById(delivery.getGuid());

    }

    @Override
    public SDeliveryListDetail selectDeliveryListByGuidDelivery(Integer guidDelivery) {

        SDelivery delivery = selectById(guidDelivery);
        if(delivery == null){
            throw new DeveloperException("没有guid为"+guidDelivery+"投放申请！");
        }
        //根本投放申请的工作项guid查询关联的信息
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, delivery.getGuidWorkitem());
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM);
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(branchMappings.size() == 0){
            throw new DeveloperException("没有找到此投放申请的工作项关联分支的信息！");
        }
        SBranch branch = branchService.selectById(branchMappings.get(0).getGuidBranch());
        if(branch == null){
            throw new DeveloperException("没有找到对应的分支信息！");
        }
        EntityWrapper<SDeliveryList> deliveryListEntityWrapper = new EntityWrapper<>();
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY, guidDelivery);
        List<SDeliveryList> deliveryLists = deliveryListService.selectList(deliveryListEntityWrapper);
        if(deliveryLists.size() == 0){
            throw new DeveloperException("没有找到投放申请下的代码清单集合！");
        }
        List<SDeliveryList> deliveryList = new ArrayList<>();
        //截掉工程前面的分支字符
        for(SDeliveryList sdl:deliveryLists){
            sdl.setFullPath(sdl.getFullPath().substring(branch.getFullPath().length()));
            deliveryList.add(sdl);
        }
        List<DeliveryProjectDetail> details = DeliveryProjectDetail.getDeliveryDetail(deliveryLists, projectService.selectProjectAll());
        SDeliveryListDetail detail = new SDeliveryListDetail();
        detail.setCount(deliveryLists.size());
        detail.setDeliveryProjectDetails(details);
        detail.setFullPath(branch.getFullPath());
        return detail;
    }

    @Override
    public List<SDelivery> selectDeliveryOutExecl(DeliveryOutExeclRequest request) {

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, request.getGuidProfile());
        deliveryEntityWrapper.eq(SDelivery.COLUMN_PACK_TIMING, request.getPackTiming());
        deliveryEntityWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(request.getDeliveryTime()));
        deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT, DeliveryResult.DELIVERED);
        List<SDelivery> deliveries = selectList(deliveryEntityWrapper);

        if (deliveries.size() == 0) {
            throw new DeveloperException("没有找到对应的投放申请！");
        }
        return deliveries;
    }

    @Override
    public void updateDelivery(SDeliveryUpdateRequest request) throws ParseException {
        SDelivery sDelivery = selectById(request.getGuidDelivery());
        if(sDelivery == null){
            throw new DeveloperException("此投放申请不存在！");
        }

        if(!sDelivery.getDeliveryResult().equals(DeliveryResult.APPLYING)){
            throw new DeveloperException("你要修改的投放申请不处于申请中状态！");
        }
        //判断投放时间及投放窗口是否合理
        Date date = new Date();
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm");
        if (ymdFormat.format(request.getDeliveryTime()).equals(ymdFormat.format(date))){
            //投放窗口的时间戳
            long time1 = hmFormat.parse(request.getPackTiming()).getTime();
            //当前时间的时间戳
            long time2 = hmFormat.parse(hmFormat.format(date)).getTime();
            if(time2 > time1){
                throw new DeveloperException("你修改投放的窗口已经过期，请选择下一个窗口投放！");
            }
        }else{
            //将投放时间转成时间戳
            long deliverTime1 = ymdFormat.parse(ymdFormat.format(request.getDeliveryTime())).getTime();
            //当前时间的时间戳
            long time2 = ymdFormat.parse(ymdFormat.format(request.getDeliveryTime())).getTime();
            if(time2 > deliverTime1){
                throw new DeveloperException("你选择的投放日期已是过去日期，请重新投放时间！");
            }
        }
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.DELIVERED);
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(request.getDeliveryTime()));
        deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING,request.getPackTiming());
        deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES,sDelivery.getGuidProfiles());
        List<SDelivery> deliveries = selectList(deliveryWrapper);
        if(deliveries.size() > 0){
            throw new DeveloperException("你选择的投放环境对应的打包窗口已完成投放，请重新选择！");
        }

        //集合添加核对中及核对成功状态，控制投放申请的环境是否可投放
        List<CheckStatus> checkStatuses = new ArrayList<>();
        checkStatuses.add(CheckStatus.WAIT);
        checkStatuses.add(CheckStatus.SUCCESS);
        //获取这个投放申请的运行环境是否有正在核对中
        EntityWrapper<SCheck> checkEntityWrapper = new EntityWrapper<>();
        checkEntityWrapper.eq(SCheck.COLUMN_GUID_PROFILES, sDelivery.getGuidProfiles());
        checkEntityWrapper.in(SCheck.COLUMN_CHECK_STATUS, checkStatuses);
        checkEntityWrapper.eq(SCheck.COLUMN_PACK_TIMING, request.getPackTiming());
        checkEntityWrapper.eq("DATE_FORMAT(" + SCheck.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(request.getDeliveryTime()));
        if(checkService.selectList(checkEntityWrapper).size() > 0){
            throw new DeveloperException("你本次选择投放的环境窗口有申请正在核对中！");
        }

        sDelivery.setDeliveryTime(request.getDeliveryTime());
        sDelivery.setPackTiming(request.getPackTiming());

        updateById(sDelivery);
    }

    @Override
    public SProfileDetail selectProfileDeteilVerify(Integer guidDelivery) throws ParseException {
        SDelivery delivery = selectById(guidDelivery);
        if(delivery == null){
            throw new DeveloperException("查询不到对应的投放申请!");
        }
        SProfiles sProfiles = profilesService.selectById(delivery.getGuidProfiles());
        if(sProfiles == null){
            throw new DeveloperException("查询不到对应的运行环境!");
        }

        SProfileDetail sProfileDetail = new SProfileDetail();
        PackTimeVerify packTimeVerify = null;
        try {
            packTimeVerify = profilesService.packTimeVerify(sProfiles.getPackTiming());
        } catch (ParseException e) {
            throw new DeveloperException("窗口不是时间格式!");
        }
        if(packTimeVerify == null){
            throw new DeveloperException("找不到此投放申请运行环境的打包窗口!");
        }
        sProfileDetail.setDeliveryTime(packTimeVerify.getDeliveryTime());
        sProfileDetail.setApplyAlias(delivery.getApplyAlias());
        sProfileDetail.setGuidProfile(sProfiles.getGuid());
        sProfileDetail.setProfilesName(sProfiles.getProfilesName());
        sProfileDetail.setPackTimeDetails(packTimeVerify.getPackTimeDetails());

        return sProfileDetail;
    }

}

