package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tis.senior.module.developer.dao.SCheckMapper;
import org.tis.senior.module.developer.entity.*;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.*;
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
    private ISCheckListService checkListService;

    @Autowired
    private ISWorkitemService workitemService;

    @Autowired
    private ISStandardListService standardListService;

    @Override
    public CheckResultDetail check(String profileId, PackTime packTiming, String userId) throws SVNException {
        // 验证环境
        SProfiles profiles = validateProfiles(profileId, packTiming);

        // 验证当前日期-环境-窗口是否有核对记录未处理
        List<SCheck> sChecks = validateCanCheck(profiles, packTiming);

        // 验证是否有未合并投放，获取该环境打包窗口的全部投产代码
        List<SDelivery> deliveryList = getProfileDeliveryList(profiles, packTiming);
        List<Integer> deliveryGuids = deliveryList.stream().map(SDelivery::getGuid).collect(Collectors.toList());
        EntityWrapper<SDeliveryList> deliveryListWrapper = new EntityWrapper<>();
        deliveryListWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, deliveryGuids);
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(deliveryListWrapper);

        // 生成核对记录
        SCheck check = new SCheck();
        check.setCheckAlias(profiles.getProfilesName()
                + "|" + new SimpleDateFormat("yyyyMMdd").format(new Date())
                + "|" + packTiming.getValue()
                + "|第" + (sChecks.size() + 1) + "次核对");
        check.setCheckDate(new Date());
        check.setCheckUser(userId);
        check.setGuidProfiles(Integer.valueOf(profileId));
        check.setCheckStatus(CheckStatus.WAIT);
        check.setDeliveryTime(new Date());
        check.setPackTiming(packTiming);
        this.baseMapper.insert(check);
        // 更新所有相关申请结果为核对中
        updateDeliveryResult(profiles, packTiming, DeliveryResult.CHECKING);

        // 获取环境分支信息
        EntityWrapper<SBranchMapping> bMWrapper = new EntityWrapper<>();
        bMWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.RELEASE.getValue());
        bMWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS, profileId);
        SBranchMapping sBranchMapping = branchMappingService.selectOne(bMWrapper);
        if (sBranchMapping == null) {
            throw new DeveloperException("环境" + profiles.getProfilesName() + "没有关联对应分支！");
        }
        EntityWrapper<SBranch> branchWrapper = new EntityWrapper<>();
        branchWrapper.eq(SBranch.COLUMN_GUID, sBranchMapping.getGuidBranch());
        SBranch sBranch = branchService.selectOne(branchWrapper);
        if (sBranch == null) {
            throw new DeveloperException("环境" + profiles.getProfilesName() + "关联分支不存在！");
        }
        // 获取环境分支下的代码
        List<SvnFile> svnFiles = svnKitService.getDiffStatus(sBranch.getFullPath(),
                sBranch.getCurrVersion().toString(), false);
        if (svnFiles.size() < 1) {
            throw new DeveloperException("环境" + profiles.getProfilesName() + "对应分支：" + sBranch.getFullPath() + "从版本\"" +
                    sBranch.getCurrVersion() + "\"开始没有文件变动！");
        }
        Map<String, SvnFile> filePathMergeListMap = svnFiles.stream()
                .collect(Collectors.toMap(f -> DeveloperUtils.getFilePath(f.getPath()), f -> f));
        // 不在合并清单中的投放清单
        List<SCheckList> notInMerge = new ArrayList<>();
        // 不在投放清单的合并清单
        List<SCheckList> notInDelivery = new ArrayList<>();
        // 遍历所有投产申请的文件清单，根据工程名开始的路径匹配，如果合并清单中有该路径，则为核对正确
        for (SDeliveryList d : sDeliveryLists) {
            String fp = DeveloperUtils.getFilePath(d.getFullPath());
            if (filePathMergeListMap.containsKey(fp)) {
                filePathMergeListMap.remove(fp);
            } else {
                SCheckList checkList = new SCheckList();
                checkList.setErrorType(CheckErrorType.Delivery);
                checkList.setGuidCheck(check.getGuid());
                checkList.setProgramName(d.getProgramName());
                checkList.setFullPath(d.getFullPath());
                checkList.setPartOfProject(d.getPartOfProject());
                checkList.setCommitType(d.getCommitType());
                checkList.setConfirmStatus(ConfirmStatus.WAIT);
                checkList.setGuidDelivery(d.getGuidDelivery());
                checkList.setDeployWhere(d.getDeployWhere());
                checkList.setPatchType(d.getPatchType());
                checkList.setGuidDeliveryList(d.getGuid());
                notInMerge.add(checkList);
            }
        }
        filePathMergeListMap.forEach((s, m) -> {
            SCheckList checkList = new SCheckList();
            checkList.setErrorType(CheckErrorType.Merge);
            checkList.setGuidCheck(check.getGuid());
            checkList.setProgramName(m.getProgramName());
            checkList.setFullPath(m.getPath());
            checkList.setPartOfProject(m.getProjectName());
            checkList.setCommitType(m.getType());
            checkList.setConfirmStatus(ConfirmStatus.WAIT);
            notInDelivery.add(checkList);
        });
        List<SCheckList> insert = new ArrayList<>();
        insert.addAll(notInMerge);
        insert.addAll(notInDelivery);
        checkListService.insertBatch(insert);
        // 组装核对结果
        return getCheckResultDetail(notInDelivery, deliveryList, notInMerge);
    }

    @Override
    public CheckResultDetail detail(String checkGuid) {
        SCheck check = selectById(checkGuid);
        if (check == null) {
            throw new DeveloperException(checkGuid + "对应核对记录不存在！");
        }
        // 获取当前核对中有问题的清单列表
        EntityWrapper<SCheckList> mergeWrapper = new EntityWrapper<>();
        mergeWrapper.eq(SCheckList.COLUMN_GUID_CHECK, checkGuid);
        mergeWrapper.eq(SCheckList.COLUMN_CONFIRM_STATUS, ConfirmStatus.WAIT);
        List<SCheckList> checkLists = checkListService.selectList(mergeWrapper);

        // 获取该核对的环境中的投放申请
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(check.getCheckDate()))
                .eq(SDelivery.COLUMN_GUID_PROFILES, check.getGuidProfiles())
                .eq(SDelivery.COLUMN_PACK_TIMING, check.getPackTiming().getValue());
        List<SDelivery> deliveryList = deliveryService.selectList(deliveryWrapper);

        //对有问题的清单按类型分类
        Map<CheckErrorType, List<SCheckList>> checkErrorTypeListMap = checkLists.stream()
                .collect(Collectors.groupingBy(SCheckList::getErrorType));
        List<SCheckList> mergeLists = checkErrorTypeListMap.get(CheckErrorType.Merge) == null ? new ArrayList<>() :
                checkErrorTypeListMap.get(CheckErrorType.Merge);
        List<SCheckList> deliveryLists = checkErrorTypeListMap.get(CheckErrorType.Delivery) == null ? new ArrayList<>() :
                checkErrorTypeListMap.get(CheckErrorType.Delivery);
        return getCheckResultDetail(mergeLists, deliveryList, deliveryLists);
    }

    @Override
    public void process(String deliveryGuid, DeliveryResult result, String desc, String userId) {
        if (!(result.equals(DeliveryResult.FAILED) && result.equals(DeliveryResult.SUCCESS))) {
            throw new DeveloperException("投放申请结果只能处理为成功或失败！");
        }
        SDelivery delivery = deliveryService.selectById(deliveryGuid);
        if (delivery == null) {
            throw new DeveloperException(deliveryGuid + "对应投放申请不存在！");
        }
        if (!delivery.getDeliveryResult().equals(DeliveryResult.CHECKING)) {
            throw new DeveloperException("投放申请'" + delivery.getApplyAlias() + "'当前状态为" +
                    delivery.getDeliveryResult().toString() + "，只能对核对中的申请作处理！");
        }
        delivery.setDeliveryResult(result);
        delivery.setDeliveryDesc(desc);
        delivery.setDeliveryTime(new Date());
        delivery.setDeliver(userId);
        deliveryService.updateById(delivery);
    }

    @Override
    public void confirm(String checkListGuid, ConfirmStatus status) {
        SCheckList l = checkListService.selectById(checkListGuid);
        if (l == null) {
            throw new DeveloperException("找不到'" + checkListGuid + "'对应的核对清单！");
        }
        SCheck check = this.baseMapper.selectById(l.getGuidCheck());
        if (!check.getCheckStatus().equals(CheckStatus.WAIT)) {
            throw new DeveloperException("当前该申请状态为'" + check.getCheckStatus().toString() +
                    "',只能对核对中状态的清单操作！");
        }
        if (status.equals(ConfirmStatus.UNDELIVERY) || status.equals(ConfirmStatus.MERGED)) {
            if (l.getErrorType().equals(CheckErrorType.Merge)) {
                throw new DeveloperException(status.toString() +  "只能对申请异常清单操作！");
            }
        } else {
            if (l.getErrorType().equals(CheckErrorType.Delivery)) {
                throw new DeveloperException(status.toString() +  "只能对合并异常清单操作！");
            }
        }
        l.setConfirmStatus(status);
        checkListService.updateById(l);
    }

    @Override
    public void confirmToDelivery(String checkListGuid, String deliveryId, String patchType, String deployWhere) {
        SCheckList l = checkListService.selectById(checkListGuid);
        if (l == null) {
            throw new DeveloperException("找不到'" + checkListGuid + "'对应的核对清单！");
        }
        SCheck check = this.baseMapper.selectById(l.getGuidCheck());
        if (!check.getCheckStatus().equals(CheckStatus.WAIT)) {
            throw new DeveloperException("当前该申请状态为'" + check.getCheckStatus().toString() +
                    "',只能对核对中状态的清单操作！");
        }
        SDelivery delivery = deliveryService.selectById(deliveryId);
        if (delivery == null) {
            throw new DeveloperException("找不到'" + deliveryId + "'对应的投放申请！");
        }
        boolean isCurrCheck = DeveloperUtils.getDayFormat(delivery.getDeliveryTime())
                .equals(DeveloperUtils.getDayFormat(check.getDeliveryTime()))
                && delivery.getGuidProfiles().equals(check.getGuid())
                && delivery.getPackTiming().equals(check.getPackTiming());
        if (!isCurrCheck) {
            throw new DeveloperException("只能将核对清单添加到此次核对中的投放申请中！");
        }

        l.setConfirmStatus(ConfirmStatus.DELIVERY);
        l.setGuidDelivery(Integer.valueOf(deliveryId));
        l.setPatchType(patchType);
        l.setDeployWhere(deployWhere);
        checkListService.updateById(l);
    }

    @Override
    public List<CheckMergeDetail> getMergeList(String profileId, PackTime packTiming) {
        List<CheckMergeDetail> result = new ArrayList<>();
        // 验证环境-窗口
        SProfiles profiles = validateProfiles(profileId, packTiming);
        // 验证当前日期-环境-窗口是否有核对记录未处理
        validateCanCheck(profiles, packTiming);
        // 获取所有投产申请
        // 获取该环境打包窗口的全部投产代码
        List<SDelivery> deliveryList = getProfileDeliveryList(profiles, packTiming);
        List<Integer> deliveryGuids = deliveryList.stream().map(SDelivery::getGuid).collect(Collectors.toList());
        List<Integer> workItemGuids = deliveryList.stream().map(SDelivery::getGuidWorkitem).collect(Collectors.toList());
        EntityWrapper<SDeliveryList> deliveryListWrapper = new EntityWrapper<>();
        deliveryListWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, deliveryGuids);
        List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(deliveryListWrapper);
        Map<Integer, List<SDeliveryList>> deliveryListMap = sDeliveryLists.stream()
                .collect(Collectors.groupingBy(SDeliveryList::getGuidDelivery));

        Map<String, Map> guidOfWhatsBranch = branchService
                .selectListByForWhatIds(BranchForWhat.WORKITEM, workItemGuids).stream()
                .collect(Collectors.toMap(map -> map.get("guidOfWhats").toString(), map -> map));
        for (SDelivery delivery : deliveryList) {
            CheckMergeDetail cmd = new CheckMergeDetail();
            cmd.setDelivery(delivery);
            cmd.setBranch(guidOfWhatsBranch.get(delivery.getGuidWorkitem().toString()).get("fullPath").toString());
            cmd.setProjectList(deliveryListMap.get(delivery.getGuid()).stream()
                    .map(SDeliveryList::getPartOfProject).distinct().collect(Collectors.toList()));
            result.add(cmd);
        }
        return result;
    }

    @Override
    public void completeCheck(String id, CheckStatus status) {
        SCheck check = this.baseMapper.selectById(id);
        if (check == null) {
            throw new DeveloperException("找不到'" + id + "'对应的核对记录！");
        }
        if (!check.getCheckStatus().equals(CheckStatus.WAIT)) {
            throw new DeveloperException("该核对记录已经处理完成，无需重复操作！");
        }
        if (status.equals(CheckStatus.WAIT)) {
            throw new DeveloperException("只能对核查记录的状态处理为成功或作废！");
        }
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(check.getDeliveryTime()));
        deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, check.getGuidProfiles());
        deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING, check.getPackTiming().getValue());
        // 处理投放结果
        if (status.equals(CheckStatus.FAILURE)) {
            // 如果核对作废，所有相关申请状态全部重置为核对中
            SDelivery delivery = new SDelivery();
            delivery.setDeliveryResult(DeliveryResult.CHECKING);
            deliveryService.update(delivery, deliveryWrapper);
            check.setCheckStatus(status);
            this.baseMapper.updateById(check);
        } else {
            // 如果核对完成
            // 判断是否有未处理清单
            EntityWrapper<SCheckList> clWrapper = new EntityWrapper<>();
            clWrapper.eq(SCheckList.COLUMN_GUID_CHECK, id);
            List<SCheckList> checkLists = checkListService.selectList(clWrapper);
            Map<ConfirmStatus, List<SCheckList>> confirmStatusListMap = checkLists.stream()
                    .collect(Collectors.groupingBy(SCheckList::getConfirmStatus));
            // 如果还有清单未确认，抛出异常
            if (confirmStatusListMap.get(ConfirmStatus.WAIT) != null) {
                throw new DeveloperException("还有" + confirmStatusListMap.get(ConfirmStatus.WAIT).size()
                        + "条核对清单未确认!");
            }
            // 如果有不投放清单,从投放申请删除
            List<SCheckList> mergedList = confirmStatusListMap.get(ConfirmStatus.UNDELIVERY);
            if (!CollectionUtils.isEmpty(mergedList)) {
                List<Integer> ids = mergedList.stream().map(SCheckList::getGuidDeliveryList)
                        .collect(Collectors.toList());
                deliveryListService.deleteBatchIds(ids);
            }
            // 如果有加入投放清单,添加到投放申请
            List<SCheckList> deliveryLists = confirmStatusListMap.get(ConfirmStatus.DELIVERY);
            if (!CollectionUtils.isEmpty(deliveryLists)) {
                List<SDeliveryList> list = new ArrayList<>(deliveryLists.size());
                for (SCheckList scl : deliveryLists) {
                    SDeliveryList sdl = new SDeliveryList();
                    sdl.setGuidDelivery(scl.getGuidDelivery());
                    sdl.setProgramName(scl.getProgramName());
                    sdl.setPatchType(scl.getPatchType());
                    sdl.setDeployWhere(scl.getDeployWhere());
                    sdl.setFullPath(scl.getFullPath());
                    sdl.setCommitType(scl.getCommitType());
                    sdl.setFromType(DeliveryListFromType.MERGE);
                    list.add(sdl);
                }
                deliveryListService.insertBatch(list);
            }
            // 所有相关申请状态为核对成功的全部置为成功投放
            SDelivery delivery = new SDelivery();
            delivery.setDeliveryResult(DeliveryResult.DELIVERED);
            deliveryWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT, DeliveryResult.SUCCESS);
            deliveryService.update(delivery, deliveryWrapper);
            // 维护标准清单
            List<SDelivery> deliveryList = deliveryService.selectList(deliveryWrapper);
            List<Integer> workIds = deliveryList.stream().map(SDelivery::getGuidWorkitem).collect(Collectors.toList());
            List<Integer> deliveryIds = deliveryList.stream().map(SDelivery::getGuid).collect(Collectors.toList());
            // 获取所有需要添加到标准清单的申请清单
            EntityWrapper<SDeliveryList> sdlWrapper = new EntityWrapper<>();
            sdlWrapper.in(SDeliveryList.COLUMN_GUID_DELIVERY, deliveryIds)
                    .ne(SDeliveryList.COLUMN_FROM_TYPE, DeliveryListFromType.STANDARD);
            List<SDeliveryList> sDeliveryLists = deliveryListService.selectList(sdlWrapper);
            if (sDeliveryLists.size() > 0) {
                List<String> collect = sDeliveryLists.stream().map(SDeliveryList::getFullPath).collect(Collectors.toList());
                EntityWrapper<SStandardList> sslWrapper = new EntityWrapper<>();
                sslWrapper.eq(SDeliveryList.COLUMN_FULL_PATH, collect);
                Map<String, SStandardList> ssdMap = standardListService.selectList(sslWrapper)
                        .stream().collect(Collectors.toMap(SStandardList::getFullPath, s -> s));
                List<SStandardList> insertsOrUpdates = new ArrayList<>();
                List<Integer> deletes = new ArrayList<>();
                for (SDeliveryList d : sDeliveryLists) {
                    SStandardList ssd = ssdMap.get(d.getFullPath());
                    if (ssd != null) {
                        // 原-现—终 (修改类型的变化)
                        // D  A  M
                        // M  A  M 理论上不出现，如果发生抛出异常
                        // A  D  X 移除
                        // M  D  D
                        // A  M  A 不处理
                        // D  M  M 理论上不出现，如果发生抛出异常// D  A  M
                        // A  A  A 不处理
                        // M  M  M 不处理
                        // D  D  D 不处理
                        if (ssd.getCommitType() != d.getCommitType()) {
                            if (ssd.getCommitType().equals(CommitType.DELETED) &&
                                    d.getCommitType().equals(CommitType.ADDED)) {
                                ssd.setCommitType(CommitType.MODIFIED);
                                insertsOrUpdates.add(ssd);
                            } else if (ssd.getCommitType().equals(CommitType.ADDED) &&
                                    d.getCommitType().equals(CommitType.DELETED)) {
                                deletes.add(ssd.getGuid());
                            } else if (ssd.getCommitType().equals(CommitType.MODIFIED) &&
                                    d.getCommitType().equals(CommitType.DELETED)) {
                                ssd.setCommitType(CommitType.DELETED);
                                insertsOrUpdates.add(ssd);
                            } else if (ssd.getCommitType().equals(CommitType.ADDED) &&
                                    d.getCommitType().equals(CommitType.MODIFIED)) {
                            } else {
                                throw new DeveloperException("标准清单发生了预料之外的异常！");
                            }
                        }
                    } else {
                        // 如果出现标准清单中没有的文件，提交类型是新增，并且导出类型是epd,需要判断添加到标准清单中是ecd或epd
                        SStandardList newSSD = new SStandardList();
                        BeanUtils.copyProperties(d, newSSD);
                        newSSD.setGuid(null);
                        if (d.getPatchType().equals(PatchType.EPD.getValue().toString())) {
                            EntityWrapper<SStandardList> wrapper = new EntityWrapper<>();
                            wrapper.like(SStandardList.COLUMN_FULL_PATH,
                                    DeveloperUtils.getModulePath(d.getFullPath()), SqlLike.LEFT)
                                    .eq(SStandardList.COLUMN_PATCH_TYPE, PatchType.ECD);
                            if (standardListService.selectCount(wrapper) > 0) {
                                newSSD.setPatchType(PatchType.ECD.getValue().toString());
                            }
                        }
                        insertsOrUpdates.add(newSSD);
                    }
                    if (insertsOrUpdates.size() > 0) {
                        standardListService.insertOrUpdateBatch(insertsOrUpdates);
                    }
                    if (deletes.size() > 0) {
                        standardListService.deleteBatchIds(deletes);
                    }
                }
            }
        }

    }

    /**
     * 获取核对结果
     *
     * @param mergeLists
     * @param deliverys
     * @param deliveryLists
     * @return
     */
    private CheckResultDetail getCheckResultDetail(List<SCheckList> mergeLists, List<SDelivery> deliverys,
                                                   List<SCheckList> deliveryLists) {
        CheckResultDetail result = new CheckResultDetail();
        result.setMergeLists(mergeLists);
        List<DeliveryCheckResultDetail> details = new ArrayList<>();
        result.setDeliveryDetails(details);
        if (deliverys.size() > 0) {
            // 获取异常申请的工作项信息
            List<Integer> workIds = deliverys.stream().map(SDelivery::getGuidWorkitem).distinct()
                    .collect(Collectors.toList());
            // 异常投放清单的map
            Map<Integer, List<SCheckList>> notInMergeListMap = deliveryLists.stream()
                    .collect(Collectors.groupingBy(SCheckList::getGuidDelivery));

            Map<Integer, SWorkitem> workItemMap = getWorkItemMap(workIds);
            for (SDelivery delivery : deliverys) {
                DeliveryCheckResultDetail detail = new DeliveryCheckResultDetail();
                detail.setDelivery(delivery);
                detail.setWorkitem(workItemMap.get(delivery.getGuidWorkitem()));
                if (notInMergeListMap.get(delivery.getGuid()) != null) {
                    detail.setDetailList(DeliveryProjectDetail.getCheckDeliveryDetail(
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
     * 验证环境及窗口是否错误，返回环境信息
     *
     * @param profileId
     * @param packTiming
     * @return
     */
    private SProfiles validateProfiles(String profileId, PackTime packTiming) {
        List<SProfiles> list = profilesService.selectProfilesAll().stream().filter(p ->
                StringUtils.equals(p.getGuid().toString(), profileId) &&
                        Arrays.asList(p.getPackTiming().split(",")).contains(packTiming.getValue().toString()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            throw new DeveloperException(profileId + packTiming.getValue() + "对应的环境或打包窗口不存在!");
        }
        return list.get(0);
    }

    /**
     * 验证当前环境及窗口能否核对，返回所有核对记录
     *
     * @param profiles
     * @param packTiming
     * @return
     */
    private List<SCheck> validateCanCheck(SProfiles profiles, PackTime packTiming) {
        EntityWrapper<SCheck> wrapper = new EntityWrapper<>();
        wrapper.eq("DATE_FORMAT(" + SCheck.COLUMN_CHECK_DATE + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .eq(SCheck.COLUMN_GUID_PROFILES, profiles.getGuid())
                .eq(SCheck.COLUMN_PACK_TIMING, packTiming.getValue())
                .orderBy(SCheck.COLUMN_GUID, false);
        List<SCheck> sChecks = this.baseMapper.selectList(wrapper);
        if (sChecks.size() > 0) {
            SCheck lastCheck = sChecks.get(0);
            if (lastCheck.getCheckStatus().equals(CheckStatus.SUCCESS)) {
                throw new DeveloperException("今天环境" + profiles.getProfilesName() + "的打包窗口"
                        + packTiming.getValue() + "已经核对成功，无需再次核对！");
            } else if (lastCheck.getCheckStatus().equals(CheckStatus.WAIT)) {
                throw new DeveloperException("今天环境" + profiles.getProfilesName() + "的打包窗口"
                        + packTiming.getValue() + "已经有一次核对记录尚未完成！");
            }
        }
        return sChecks;
    }

    /**
     * 获取环境-打包窗口下所有投产申请
     *
     * @param profiles
     * @param packTiming
     * @return
     */
    private List<SDelivery> getProfileDeliveryList(SProfiles profiles, PackTime packTiming) {
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, profiles.getGuid());
        deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING, packTiming.getValue());
        List<SDelivery> deliveryList = deliveryService.selectList(deliveryWrapper);
        if (CollectionUtils.isEmpty(deliveryList)) {
            throw new DeveloperException("当天环境" + profiles.getProfilesName() + "的打包窗口" + packTiming.getValue() +
                    "没有投产申请记录!");
        }
        if (deliveryList.stream().anyMatch(d -> d.getDeliveryResult().equals(DeliveryResult.APPLYING))) {
            throw new DeveloperException("存在没有合并的投放申请，全部合并才能核对！");
        }
        return deliveryList;
    }

    private void updateDeliveryResult(SProfiles profiles, PackTime packTiming, DeliveryResult deliveryResult) {
        SDelivery updateDelivery = new SDelivery();
        updateDelivery.setDeliveryResult(deliveryResult);
        EntityWrapper<SDelivery> deliveryWrapper = new EntityWrapper<>();
        deliveryWrapper.eq("DATE_FORMAT(" + SDelivery.COLUMN_DELIVERY_TIME + ", '%Y-%m-%d')",
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deliveryWrapper.eq(SDelivery.COLUMN_GUID_PROFILES, profiles.getGuid());
        deliveryWrapper.eq(SDelivery.COLUMN_PACK_TIMING, packTiming.getValue());
        deliveryService.update(updateDelivery, deliveryWrapper);
    }

}

