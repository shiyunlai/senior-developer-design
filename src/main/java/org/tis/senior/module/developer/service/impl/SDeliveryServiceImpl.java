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
import org.tis.senior.module.developer.entity.enums.DeliveryType;
import org.tis.senior.module.developer.entity.enums.PatchType;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;

import javax.swing.text.html.parser.Entity;
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
        Map<PatchType, Integer> patchCount = new HashMap<>(5);
        details.forEach(d -> d.getPatchName().forEach(p -> {
            if (patchCount.get(p) != null) {
                patchCount.put(p, 1);
            } else {
                patchCount.put(p, patchCount.get(p) + 1);
            }
        }));
        // 投放合计
        Map<String, Integer> deliveryCount = new HashMap<>(3);
        deliveryCount.put(DeliveryDetail.TOTAL_FILE, sDeliveryLists.size());
        deliveryCount.put(DeliveryDetail.TOTAL_PATCH, patchCount.values().stream().reduce(0, Integer::sum));
        // TODO 脚本数的统计
        // deliveryCount.put(DeliveryDetail.TOTAL_SCRIPT, )

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
        List<SDelivery> insert = new ArrayList<>(deliveryList.size());
        // 合并为一个投产申请,每个环境形成一个独立的投放申请
        mergeDelivery.getProfiles().forEach(p -> {
            SDelivery sDelivery = new SDelivery();
            sDelivery.setGuidProfiles(p.getGuidProfiles());
            sDelivery.setPackTiming(p.getPackTiming());
            sDelivery.setDeliveryType(DeliveryType.MERGE);
            sDelivery.setMergeList(mergeDelivery.getMergeList().stream().reduce("", (r, s) -> r + "," + s));
            sDelivery.setApplyAlias(deliveryList.stream()
                    .map(SDelivery::getApplyAlias).reduce("合并投放", (r, s) -> r + "，" + s));
            sDelivery.setApplyTime(new Date());
            sDelivery.setProposer(userId);
            insert.add(sDelivery);
        });
        // FIXME 合并申请与普通申请,其代码清单是相同的，所以无需再生成新清单
        insertBatch(insert);
    }

    /**
     * 判断是否允许合并投放，返回合并申请信息集合
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
}

