package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.developer.dao.SCheckMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryCheckResultDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
import org.tis.senior.module.developer.entity.vo.SvnFile;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.*;
import org.tis.senior.module.developer.util.DeveloperUtils;
import org.tmatesoft.svn.core.SVNException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sCheck的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SCheckServiceImpl extends ServiceImpl<SCheckMapper, SCheck> implements ISCheckService {

    @Autowired
    private ISProfilesService profilesService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISSvnKitService svnKitService;

    @Autowired
    private ISDeliveryService deliveryService;

    @Autowired
    private ISDeliveryListService deliveryListService;

    @Autowired
    private ISMergeListService mergeListService;

    @Autowired
    private ISWorkitemService workitemService;

    @Override
    public CheckResultDetail check(String profileId, PackTime packTiming, String userId) throws SVNException {
        // 验证环境
        List<SProfiles> list = profilesService.selectProfilesAll().stream().filter(p ->
                StringUtils.equals(p.getGuid().toString(), profileId) &&
                        Arrays.asList(p.getPackTiming().split(",")).contains(packTiming.getValue().toString()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            throw new DeveloperException(profileId + packTiming.getValue() + "对应的环境或打包窗口不存在!");
        }
        SProfiles profiles = list.get(0);

        // 与投放申请核对
        // 获取该环境打包窗口的全部投产代码
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, profileId);
        deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING, packTiming.getValue());
        List<SDelivery> deliveryList = deliveryService.selectList(deliveryWrapper);
        if (CollectionUtils.isEmpty(deliveryList)) {
            throw new DeveloperException("当天环境" +  profiles.getProfilesName() + "的打包窗口" + packTiming.getValue() +
                    "没有投产申请记录!");
        }

        List<Integer> deliveryGuids = deliveryList.stream().map(SDelivery::getGuid).collect(Collectors.toList());
        EntityWrapper<SDeliveryList> deliveryListWrapper = new EntityWrapper<>();
        deliveryListWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, deliveryGuids);
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(deliveryListWrapper);

        // 生成核对记录
        SCheck check = new SCheck();
        check.setCheckAlias(genCheckAlias(profiles, packTiming));
        check.setCheckDate(new Date());
        check.setCheckUser(userId);
        check.setGuidProfiles(Integer.valueOf(profileId));
        check.setCheckStatus(CheckStatus.WAIT);
        check.setPackTiming(packTiming);
        this.baseMapper.insert(check);

        // 获取环境分支信息
        EntityWrapper<SBranchMapping> bMWrapper = new EntityWrapper<>();
        bMWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.RELEASE.getValue());
        bMWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, profileId);
        SBranchMapping sBranchMapping = branchMappingService.selectOne(bMWrapper);
        EntityWrapper<SBranch> branchWrapper = new EntityWrapper<>();
        branchWrapper.eq(SBranch.COLUMN_GUID, sBranchMapping.getGuidBranch());
        SBranch sBranch = branchService.selectOne(branchWrapper);

        // 获取环境分支下的代码
        List<SMergeList> mergeLists = new ArrayList<>();
        List<SvnFile> svnFiles = svnKitService.getDiffStatus(sBranch.getFullPath(), sBranch.getCurrVersion().toString());
        if (svnFiles.size() < 1) {
            throw new DeveloperException("分支" + sBranch.getFullPath() + "从版本\"" +
                    sBranch.getCurrVersion() + "\"开始没有文件变动！");
        }
        svnFiles.forEach(f -> {
            if ("file".equals(f.getNodeType())) {
                SMergeList merge = new SMergeList();
                merge.setAuthor(f.getAuthor());
                merge.setCheckGuid(check.getGuid());
                merge.setProgramName(f.getProgramName());
                merge.setMergeVersion(f.getRevision().intValue());
                merge.setFullPath(f.getPath());
                merge.setMergeDate(f.getData());
                merge.setMergeType(f.getType());
                merge.setPartOfProject(f.getProjectName());
                merge.setDeveloperConfirm(ConfirmStatus.WAIT);
                mergeLists.add(merge);
            }
        });
        Map<String, SMergeList> filePathMergeListMap = mergeLists.stream()
                .collect(Collectors.toMap(me -> DeveloperUtils.getFilePath(me.getFullPath()), merge -> merge));

        // 不在合并清单中的投放清单
        List<SDeliveryList> notInMerge = new ArrayList<>();
        // 不在合并清单中的id
        List<Integer> notInMergeIds = new ArrayList<>();
        // 在合并清单中的id
        List<Integer> inMergeIds = new ArrayList<>();

        for(SDeliveryList d : sDeliveryLists) {
            String fp = DeveloperUtils.getFilePath(d.getFullPath());
            if (filePathMergeListMap.containsKey(fp)) {
                inMergeIds.add(d.getGuid());
                filePathMergeListMap.get(fp).setDeveloperConfirm(ConfirmStatus.CONFIRM);
                filePathMergeListMap.remove(fp);
            } else {
                notInMergeIds.add(d.getGuid());
                notInMerge.add(d);
            }
        }
        filePathMergeListMap.forEach( (s,m) -> m.setDeveloperConfirm(ConfirmStatus.DISCUSS));

        // 如果投产清单中有代码不在合并清单中
        if (notInMerge.size() > 0) {
            List<Integer> list1 = notInMerge.stream().map(SDeliveryList::getGuidDelivery).
                    distinct().collect(Collectors.toList());
            updateDeliveryResult(list1, DeliveryResult.FAILED);
            deliveryGuids.removeAll(list1);
            updateDeliveryListConfirmStatus(notInMergeIds, ConfirmStatus.DISCUSS);
        }
        // 变更投产状态
        updateDeliveryResult(deliveryGuids, DeliveryResult.SUCCESS);
        updateDeliveryListConfirmStatus(inMergeIds, ConfirmStatus.CONFIRM);
        // 不在投产代码中的合并清单
        List<SMergeList> notInDelivery = new ArrayList<>(filePathMergeListMap.values());
        // 不在合并代码中的投放申请
//        deliveryList.removeIf(d -> deliveryGuids.contains(d.getGuid()));
        // 插入合并清单
        mergeListService.insertBatch(mergeLists);
        // 变更分支版本
        sBranch.setCurrVersion(svnKitService.getLastRevision(sBranch.getFullPath()));
        branchService.updateById(sBranch);

        // 组装核对结果
        return getCheckResultDetail(notInDelivery, deliveryList, notInMerge);
    }


    @Override
    public CheckResultDetail detail(String checkGuid) {
        SCheck check = selectById(checkGuid);
        if (check == null) {
            throw new DeveloperException(checkGuid + "对应核对记录不存在！");
        }
        // 获取当前核对中合并清单中有问题的清单列表
        EntityWrapper<SMergeList> mergeWrapper = new EntityWrapper<>();
        mergeWrapper.eq(SMergeList.COLUMN_CHECK_GUID, checkGuid);
        mergeWrapper.eq(SMergeList.COLUMN_DEVELOPER_CONFIRM, ConfirmStatus.DISCUSS);
        List<SMergeList> mergeLists = mergeListService.selectList(mergeWrapper);

        // 获取该核对的环境中的投放申请
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                  new SimpleDateFormat("yyyy-MM-dd").format(check.getCheckDate()))
                .eq(SDelivery.COLUMN_PACK_TIMING, check.getPackTiming().getValue());
        List<SDelivery> deliveryList = deliveryService.selectList(deliveryWrapper);

        //获取申请对应的有问题的投放清单
        EntityWrapper<SDeliveryList> deliveryListWrapper = new EntityWrapper<>();
        deliveryListWrapper.eq(SDeliveryList.COLUMN_DEVELOPER_CONFIRM, ConfirmStatus.DISCUSS)
                .in(SDeliveryList.COLUMN_GUID_DELIVERY, deliveryList.stream().map(SDelivery::getGuid)
                        .collect(Collectors.toList()));
        List<SDeliveryList> deliveryLists = deliveryListService.selectList(deliveryListWrapper);

        return getCheckResultDetail(mergeLists, deliveryList, deliveryLists);
    }

    @Override
    public void process(String deliveryGuid, DeliveryResult result, String desc, String userId) {
        SDelivery delivery = deliveryService.selectById(deliveryGuid);
        if (delivery == null) {
            throw new DeveloperException(deliveryGuid + "对应投放申请不存在！");
        }
        if (!delivery.getDeliveryResult().equals(DeliveryResult.APPLYING)) {
            throw new DeveloperException(deliveryGuid + "对应的投放申请结果已经处理为" +
                    delivery.getDeliveryResult().toString());
        }
        delivery.setDeliveryResult(result);
        delivery.setDeliveryDesc(desc);
        delivery.setDeliveryTime(new Date());
        delivery.setDeliver(userId);
        deliveryService.updateById(delivery);
    }

    @Override
    public void confirm(boolean isDelivery, String id) {
        if (isDelivery) {
            SDeliveryList l = deliveryListService.selectById(id);
            if (l == null) {
                throw new DeveloperException("找不到" + id + "对应的投放清单！");
            }
            if (!l.getDeveloperConfirm().equals(ConfirmStatus.DISCUSS)) {
                throw new DeveloperException("该投产清单已经被确认或所在投产申请尚未核对！");
            }
            l.setDeveloperConfirm(ConfirmStatus.CONFIRM);
            deliveryListService.updateById(l);
        } else {
            SMergeList l = mergeListService.selectById(id);
            if (l == null) {
                throw new DeveloperException("找不到" + id + "对应的合并清单！");
            }
            if (!l.getDeveloperConfirm().equals(ConfirmStatus.DISCUSS)) {
                throw new DeveloperException("该合并清单已经被确认！");
            }
            l.setDeveloperConfirm(ConfirmStatus.CONFIRM);
            mergeListService.updateById(l);
        }
    }

    /**
     * 获取核对结果
     * @param mergeLists
     * @param deliverys
     * @param deliveryLists
     * @return
     */
    private CheckResultDetail getCheckResultDetail(List<SMergeList> mergeLists, List<SDelivery> deliverys,
                                                   List<SDeliveryList> deliveryLists) {
        CheckResultDetail result = new CheckResultDetail();
        result.setMergeLists(mergeLists);
        List<DeliveryCheckResultDetail> details = new ArrayList<>();
        result.setDeliveryDetails(details);
        if (deliverys.size() > 0) {
            // 获取异常申请的工作项信息
            List<Integer> workIds = deliverys.stream().map(SDelivery::getGuidWorkitem).distinct()
                    .collect(Collectors.toList());
            // 异常投放清单的map
            Map<Integer, List<SDeliveryList>> notInMergeListMap = deliveryLists.stream()
                    .collect(Collectors.groupingBy(SDeliveryList::getGuidDelivery));

            Map<Integer, SWorkitem> workItemMap = getWorkItemMap(workIds);
            for (SDelivery delivery : deliverys) {
                DeliveryCheckResultDetail detail = new DeliveryCheckResultDetail();
                detail.setDelivery(delivery);
                detail.setWorkitem(workItemMap.get(delivery.getGuidWorkitem()));
                if (notInMergeListMap.get(delivery.getGuid()) != null) {
                    detail.setDetailList(DeliveryProjectDetail.getDeliveryDetail(
                            notInMergeListMap.get(delivery.getGuid())));
                }
                details.add(detail);
            }
        }
        return result;

    }


    private Map<Integer, SWorkitem> getWorkItemMap(List<Integer> list) {
        EntityWrapper<SWorkitem> wrapper = new EntityWrapper<>();
        wrapper.in(SWorkitem.COLUMN_GUID, list);
        return workitemService.selectList(wrapper).stream().collect(Collectors.toMap(SWorkitem::getGuid, w -> w));
    }

    /**
     * 更新投放清单确认状态
     * @param list
     * @param result
     */
    private void updateDeliveryResult(List<Integer> list, DeliveryResult result) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        SDelivery d = new SDelivery();
        d.setDeliveryResult(result);
        EntityWrapper<SDelivery> wrapper = new EntityWrapper<>();
        wrapper.in(SDelivery.COLUMN_GUID, list);
        deliveryService.update(d, wrapper);
    }

    /**
     * 更新投放申请确认状态
     * @param list
     * @param status
     */
    private void updateDeliveryListConfirmStatus(List<Integer> list, ConfirmStatus status) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        SDeliveryList d = new SDeliveryList();
        d.setDeveloperConfirm(status);
        EntityWrapper<SDeliveryList> wrapper = new EntityWrapper<>();
        wrapper.in(SDeliveryList.COLUMN_GUID, list);
        deliveryListService.update(d, wrapper);
    }

    private String genCheckAlias(SProfiles profiles, PackTime packTime) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 获取次数
        EntityWrapper<SCheck> wrapper = new EntityWrapper<>();
        wrapper.eq("DATE_FORMAT(" + SCheck.COLUMN_CHECK_DATE + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        wrapper.eq(SCheck.COLUMN_GUID_PROFILES, profiles.getGuid());
        wrapper.eq(SCheck.COLUMN_PACK_TIMING, packTime.getValue());
        Integer count = this.baseMapper.selectCount(wrapper) + 1;
        return profiles.getProfilesName() + "|" + date + "|" + packTime.getValue() + "|第" + count + "次核对";
    }

}

