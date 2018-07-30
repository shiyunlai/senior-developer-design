package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.core.config.SvnProperties;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.WorkitemAndBranchAddRequest;
import org.tis.senior.module.developer.controller.request.WorkitemBranchDetailRequest;
import org.tis.senior.module.developer.dao.SWorkitemMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.ProjectDetail;
import org.tis.senior.module.developer.entity.vo.WorkitemBranchDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.tmatesoft.svn.core.SVNException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sWorkitem的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SWorkitemServiceImpl extends ServiceImpl<SWorkitemMapper, SWorkitem> implements ISWorkitemService {

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Autowired
    private ISDeliveryService deliveryService;

    @Autowired
    private ISSvnKitService svnKitService;

    @Autowired
    private ISProjectService projectService;

    @Autowired
    private SvnProperties svnProperties;

    private Map<String, SWorkitem> workitems = new HashMap<>();

    @Override
    public List<SWorkitem> selectWorkitemByUser(String userId) {

        EntityWrapper<SWorkitem> sWorkitemEntityWrapper = new EntityWrapper<>();
        sWorkitemEntityWrapper.like(SWorkitem.COLUMN_OWNER,userId);
        sWorkitemEntityWrapper.eq(SWorkitem.COLUMN_ITEM_STATUS,ItemStatus.DEVELOP);
        List<SWorkitem> swList = selectList(sWorkitemEntityWrapper);
        return swList;
    }

    @Override
    public SWorkitem selectOneById(String workitemId) {

        SWorkitem workitem = this.workitems.get(workitemId);
        if(workitem != null){
            return workitem;
        }
        workitem = selectById(workitemId);
        this.workitems.put(workitem.getGuid().toString(),workitem);
        return workitem;
    }

    @Override
    public void insertWorkitemAndBranch(WorkitemAndBranchAddRequest request) {

        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request.getWorkitemUpdateRequest(),workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        insert(workitem);

        SBranch branch = new SBranch();
        BeanUtils.copyProperties(request.getBranchAddRequest(),branch);
        branchService.insert(branch);

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(branch.getGuid());
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(workitem.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void insertWorkitemAndBranchMapping(WorkitemAddAndUpdateRequest request, Integer guidBranch) {
        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request,workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        insert(workitem);

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() > 0){
            throw new DeveloperException("此分支已被指配，请重新选择分支！");
        }

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(guidBranch);
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(workitem.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void deleteWorkitemAndBranchMapping(Integer guidWorkitem) {

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidWorkitem);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.WORKITEM);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if (sBranchMappings.size() > 0) {
            SBranchMapping sbm = sBranchMappings.get(0);

            //删除对应的第三张关联表
            branchMappingService.deleteById(sbm.getGuid());
        }
        //删除guid对应的工作项
        deleteById(guidWorkitem);
    }

    @Override
    public WorkitemBranchDetailRequest workitemDetail(Integer guidWorkitem) {
        WorkitemBranchDetailRequest request = new WorkitemBranchDetailRequest();

        SWorkitem sWorkitem = selectById(guidWorkitem);
        request.setWorkitem(sWorkitem);

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,sWorkitem.getGuid());
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() != 1){
            request.setBranch(null);
        }else {
            SBranchMapping branchMapping = sbmList.get(0);
            SBranch branch = branchService.selectById(branchMapping.getGuidBranch());
            request.setBranch(branch);
        }
        return request;
    }

    @Override
    public void workitemRelevanceBranch(Integer guidWorkitem, Integer guidBranch) {

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sBranchMappings.size() > 0){
            throw new DeveloperException("此分支已被指配，请重新选择分支！");
        }

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper2 = new EntityWrapper<>();
        branchMappingEntityWrapper2.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidWorkitem);
        branchMappingEntityWrapper2.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> sBranchMappings2 = branchMappingService.selectList(branchMappingEntityWrapper2);
        if(sBranchMappings2.size() > 0){
            throw new DeveloperException("此工作项已关联分支！");
        }

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(guidBranch);
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(guidWorkitem);
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void workitemCancelBranch(Integer guidWorkitem) {
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidWorkitem);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() != 1){
            throw new DeveloperException("此工作项没有关联分支！");
        }

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,guidWorkitem);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.APPLYING);
        if(deliveryService.selectList(deliveryEntityWrapper).size() > 0){
            throw new DeveloperException("此工作项关联的分支有投放申请，不允许取消关联！");
        }

        SBranchMapping branchMapping = sbmList.get(0);
        branchMappingService.deleteById(branchMapping.getGuid());
    }

    @Override
    public SBranch selectBranchByWorkitemGuid(Integer workitemGuid) {
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,workitemGuid);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(branchMappings.size() != 1){
            throw new DeveloperException("此工作项没有分配分支信息！");
        }
        SBranchMapping branchMapping = branchMappings.get(0);
        return branchService.selectById(branchMapping.getGuidBranch());
    }

    @Override
    public List<SBranch> mayRelevanceBranch() {

        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(null);
        List<Integer> branchGuid = new ArrayList<>();
        if(sBranchMappings.size() > 0){
            for (SBranchMapping branchMapping:sBranchMappings){
                branchGuid.add(branchMapping.getGuidBranch());
            }
            if(branchGuid.size() == 0){
                throw new DeveloperException("没有查询到未被指配的分支");
            }
        }
        EntityWrapper<SBranch> branchEntityWrapper = new EntityWrapper<>();
        branchEntityWrapper.notIn(SBranch.COLUMN_GUID,branchGuid);
        branchEntityWrapper.ne(SBranch.COLUMN_BRANCH_TYPE,BranchType.RELEASE);
        return branchService.selectList(branchEntityWrapper);
    }

    @Override
    public Page<WorkitemBranchDetail> workitemFullPathDetail(Page<WorkitemBranchDetail> page,
                                                             EntityWrapper<WorkitemBranchDetail> wrapper) {

        return page.setRecords(this.baseMapper.selectWorkitemDetail(page, wrapper));
    }

    @Override
    public void updateStatus(Integer guidWorkitem) {
        SWorkitem sWorkitem = selectById(guidWorkitem);
        if(sWorkitem.getItemStatus().equals(ItemStatus.CANCEL)){
            throw new DeveloperException("此工作项已关闭！");
        }

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.ne(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.DELIVERED);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,sWorkitem.getGuid());
        if(deliveryService.selectList(deliveryEntityWrapper).size() > 0){
            throw new DeveloperException("此工作项有投放申请未完成操作，不能关闭！");
        }

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,sWorkitem.getGuid());
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.WORKITEM);
        if(branchMappingService.selectList(branchMappingEntityWrapper).size() > 0){
            throw new DeveloperException("此工作项有关联的分支，不能关闭！");
        }


        sWorkitem.setItemStatus(ItemStatus.CANCEL);
        updateById(sWorkitem);
    }

    @Override
    public void updateStatusPutProduct(Integer guidWorkitem) {
        SWorkitem sWorkitem = selectById(guidWorkitem);

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.ne(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.DELIVERED);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_WORKITEM,sWorkitem.getGuid());
        if(deliveryService.selectList(deliveryEntityWrapper).size() > 0){
            throw new DeveloperException("此工作项有投放申请未完成操作，不能修改成已投产！");
        }
        sWorkitem.setItemStatus(ItemStatus.CANCEL);
        updateById(sWorkitem);
    }

    @Override
    public void insertBranch(String guid, SBranch branch) throws SVNException {
        SWorkitem sWorkitem = selectById(guid);
        if (sWorkitem == null) {
            throw new DeveloperException(guid + "对应工作项已删除或不存在！");
        }
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper
                .eq(SBranchMapping.COLUMN_GUID_OF_WHATS, guid)
                .eq(SBranchMapping.COLUMN_FOR_WHAT, branch.getBranchType());
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if (!CollectionUtils.isEmpty(branchMappings)) {
            throw new DeveloperException("该工作项已经关联分支，不能重复添加！");
        }
        String message = String.format("[artf%s]:建分支", sWorkitem.getArtf());
        String fullPath = (branch.getBranchType().equals(BranchType.FEATURE) ? svnProperties.getBaseFeatureUrl()
                : svnProperties.getBaseHotfixUrl()) + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        branch.setFullPath(fullPath);
        long revision = svnKitService.doMkDir(branch.getFullPath(), message);
        try {
            branch.setCurrVersion((int) revision);
            branch.setLastVersion((int) revision);
            branchService.insert(branch);
            SBranchMapping sBranchMapping = new SBranchMapping();
            sBranchMapping.setAllotTime(new Date());
            sBranchMapping.setForWhat(BranchForWhat.WORKITEM);
            sBranchMapping.setStatus(BranchMappingStatus.TAKE);
            sBranchMapping.setGuidBranch(branch.getGuid());
            sBranchMapping.setGuidOfWhats(Integer.valueOf(guid));
            branchMappingService.insert(sBranchMapping);
        } catch (Exception e) {
            svnKitService.doDelete(branch.getFullPath(), message + "异常回滚！");
            throw e;
        }
    }

    @Override
    public void insertProjects(String guid, List<String> projectGuids) throws SVNException {
        SWorkitem sWorkitem = selectById(guid);
        if (sWorkitem == null) {
            throw new DeveloperException(guid + "对应工作项已删除或不存在！");
        }
        List<Map> maps = branchService.selectListByForWhatIds(BranchForWhat.WORKITEM, Collections.singletonList(guid));
        if (CollectionUtils.isEmpty(maps)) {
            throw new DeveloperException(guid + "对应工作项没有关联分支");
        }
        String destUrl = maps.get(0).get(SBranch.COLUMN_FULL_PATH).toString();
        EntityWrapper<SProject> wrapper = new EntityWrapper<>();
        wrapper.in(SProject.COLUMN_GUID, projectGuids);
        List<SProject> sProjects = projectService.selectList(wrapper);
        String[] sourceUrls = new String[sProjects.size()];
        for (int i = 0; i < sProjects.size(); i++) {
            if (!projectGuids.contains(sProjects.get(i).getGuid().toString())) {
                throw new DeveloperException(guid + "对应工程已删除或不存在！");
            }
            sourceUrls[i] = sProjects.get(i).getFullPath();
        }
        String message = String.format("[artf%s]:拉工程", sWorkitem.getArtf());
        svnKitService.doCopy(sourceUrls, destUrl, message);
    }

    @Override
    public ProjectDetail selectProjects(String guid) throws SVNException {
        SWorkitem sWorkitem = selectById(guid);
        if (sWorkitem == null) {
            throw new DeveloperException(guid + "对应工作项已删除或不存在！");
        }
        List<Map> maps = branchService.selectListByForWhatIds(BranchForWhat.WORKITEM, Collections.singletonList(guid));
        if (CollectionUtils.isEmpty(maps)) {
            throw new DeveloperException(guid + "对应工作项没有关联分支");
        }
        List<String> dir = svnKitService.getDir(maps.get(0).get(SBranch.COLUMN_FULL_PATH).toString());
        List<SProject> sProjects = projectService.selectProjectAll();
        Map<Boolean, List<SProject>> collect = sProjects.stream()
                .collect(Collectors.groupingBy(p -> dir.contains(p.getProjectName())));
        ProjectDetail projectDetail = new ProjectDetail();
        projectDetail.setOwn(collect.get(true));
        projectDetail.setOwn(collect.get(false));
        return projectDetail;
    }
}

