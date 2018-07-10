package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.dao.SDeliveryMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.DeliveryType;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;

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
    public void mergeDeliver(MergeDeliveryRequest mergeDelivery, String userId) {
        List<SDelivery> deliveryList = isAllowMerge(mergeDelivery.getMergeList());
        EntityWrapper<SDeliveryList> wrapper = new EntityWrapper<>();
        wrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, mergeDelivery.getMergeList());
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(wrapper);
        // 合并为一个投产申请,每个环境形成一个独立的投放申请
        mergeDelivery.getProfiles().forEach(p -> {
            SDelivery sDelivery = new SDelivery();
            sDelivery.setGuidProfiles(Integer.parseInt(p.getGuidProfiles()));
            sDelivery.setPackTiming(p.getPackTiming());
            sDelivery.setDeliveryType(DeliveryType.MERGE);
            sDelivery.setMergeList(mergeDelivery.getMergeList().stream().reduce("", (r, s) -> r + "," + s));
            String appAlias = deliveryList.stream()
                    .map(SDelivery::getApplyAlias).reduce("合并投放:", (r, s) -> r + s + "，");
            sDelivery.setApplyAlias(appAlias.substring(0, appAlias.length() -1));
            sDelivery.setApplyTime(new Date());
            sDelivery.setProposer(userId);
            sDelivery.setDeliveryResult(DeliveryResult.APPLYING);
            insert(sDelivery);
            sDeliveryLists.forEach(s -> s.setGuidDelivery(sDelivery.getGuid()));
            deliveryListService.insertBatch(sDeliveryLists);
        });

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
        deliveryListEntityWrapper.eq(SDeliveryList.COLUMN_GUID_DELIVERY,delivery.getGuid());
        List<SDeliveryList> deliveryLists = deliveryListService.selectList(deliveryListEntityWrapper);
        Set<String> str = new HashSet<>();
        for (SDeliveryList deliveryList:deliveryLists){
            str.add(deliveryList.getPartOfProject());
        }
        projectNameList.addAll(str);
        return projectNameList;
    }

}

