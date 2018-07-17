package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.senior.module.developer.controller.request.ProfileAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.ProfileAndBranchAddRequest;
import org.tis.senior.module.developer.dao.SProfilesMapper;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.*;
import org.tis.senior.module.developer.entity.vo.ProfileBranchDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.tis.senior.module.developer.service.ISBranchService;
import org.tis.senior.module.developer.service.ISDeliveryService;
import org.tis.senior.module.developer.service.ISProfilesService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * sProfiles的Service接口实现类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SProfilesServiceImpl extends ServiceImpl<SProfilesMapper, SProfiles> implements ISProfilesService {

    private List<SProfiles> spList;

    @Autowired
    private ISBranchService branchService;

    @Autowired
    private ISBranchMappingService branchMappingService;

    @Autowired
    private ISDeliveryService deliveryService;

    @Override
    public List<SProfiles> selectProfilesAll() {
        EntityWrapper<SProfiles> spEntityWrapper = new EntityWrapper<>();
        spEntityWrapper.eq(SProfiles.COLUMN_IS_ALLOW_DELIVERY,IsAllowDelivery.ALLOW);
        return selectList(spEntityWrapper);
    }

    @Override
    public SProfiles selectOneById(String guidProfile) {
        for (SProfiles sProfiles : selectProfilesAll()) {
            if(guidProfile.equals(sProfiles.getGuid().toString())){
                return sProfiles;
            }
        }
        return null;
    }

    @Override
    public void insertProfileAndBranch(ProfileAndBranchAddRequest request) {
        SProfiles profiles = new SProfiles();
        BeanUtils.copyProperties(request.getProfileAddRequest(),profiles);
        profiles.setProfilesCode(profiles.getProfilesName());
        profiles.setIsAllowDelivery(IsAllowDelivery.ALLOW);
        insert(profiles);

        SBranch branch = new SBranch();
        BeanUtils.copyProperties(request.getBranchAddRequest(),branch);
        branchService.insert(branch);

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(branch.getGuid());
        branchMapping.setForWhat(BranchForWhat.WORKITEM);
        branchMapping.setGuidOfWhats(profiles.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void deleteProfileAndBranchMapping(Integer profileGuid) {

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,profileGuid);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT, BranchForWhat.RELEASE);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if (sBranchMappings.size() > 0) {
            SBranchMapping sbm = sBranchMappings.get(0);

            EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
            deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES,sbm.getGuidOfWhats());
            deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.APPLYING);
            if(deliveryService.selectList(deliveryEntityWrapper).size() > 0){
                throw new DeveloperException("此运行环境有对应的投放申请在申请中，不允许删除！");
            }

            //删除对应的第三张关联表
            branchMappingService.deleteById(sbm.getGuid());
        }
        //删除guid对应的运行环境对象
        deleteById(profileGuid);
    }

    @Override
    public void updateProfileStatus(Integer profileGuid, IsAllowDelivery isAllowDelivery) {
        SProfiles profiles = selectById(profileGuid);
        profiles.setIsAllowDelivery(isAllowDelivery);
        updateById(profiles);
    }

    @Override
    public void insertProfileBranchMapping(ProfileAddAndUpdateRequest request, Integer guidBranch) {
        SProfiles sProfiles = new SProfiles();
        BeanUtils.copyProperties(request,sProfiles);
        sProfiles.setIsAllowDelivery(IsAllowDelivery.ALLOW);
        insert(sProfiles);

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() > 0){
            throw new DeveloperException("此分支已被指配，请重新选择分支！");
        }
        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(guidBranch);
        branchMapping.setForWhat(BranchForWhat.RELEASE);
        branchMapping.setGuidOfWhats(sProfiles.getGuid());
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void profileRelevanceBranch(Integer guidProfile, Integer guidBranch) {

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_BRANCH,guidBranch);
        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sBranchMappings.size() > 0){
            throw new DeveloperException("此分支已被指配，请重新选择分支！");
        }

        EntityWrapper<SBranchMapping> branchMappingEntityWrapper2 = new EntityWrapper<>();
        branchMappingEntityWrapper2.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidProfile);
        branchMappingEntityWrapper2.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.RELEASE);
        List<SBranchMapping> sBranchMappings2 = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sBranchMappings2.size() > 0){
            throw new DeveloperException("此运行环境已有关联分支！");
        }

        SBranchMapping branchMapping = new SBranchMapping();
        branchMapping.setGuidBranch(guidBranch);
        branchMapping.setForWhat(BranchForWhat.RELEASE);
        branchMapping.setGuidOfWhats(guidProfile);
        branchMapping.setAllotTime(new Date());
        branchMapping.setStatus(BranchMappingStatus.TAKE);
        branchMappingService.insert(branchMapping);
    }

    @Override
    public void profileCancelBranch(Integer guidProfile) {
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,guidProfile);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.RELEASE);
        List<SBranchMapping> sbmList = branchMappingService.selectList(branchMappingEntityWrapper);
        if(sbmList.size() != 1){
            throw new DeveloperException("此运行环境没有分配分支,请关联分支！");
        }

        EntityWrapper<SDelivery> deliveryEntityWrapper = new EntityWrapper<>();
        deliveryEntityWrapper.eq(SDelivery.COLUMN_GUID_PROFILES,guidProfile);
        deliveryEntityWrapper.eq(SDelivery.COLUMN_DELIVERY_RESULT,DeliveryResult.APPLYING);
        if(deliveryService.selectList(deliveryEntityWrapper).size() > 0){
            throw new DeveloperException("此运行环境关联的分支有投放申请，不允许取消关联！");
        }

        SBranchMapping branchMapping = sbmList.get(0);
        branchMappingService.deleteById(branchMapping.getGuid());
    }

    @Override
    public SBranch selectBranchByProfileGuid(Integer profileGuid) {
        EntityWrapper<SBranchMapping> branchMappingEntityWrapper = new EntityWrapper<>();
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_GUID_OF_WHATS,profileGuid);
        branchMappingEntityWrapper.eq(SBranchMapping.COLUMN_FOR_WHAT,BranchForWhat.RELEASE);
        List<SBranchMapping> branchMappings = branchMappingService.selectList(branchMappingEntityWrapper);
        if(branchMappings.size() != 1){
            throw new DeveloperException("此运行环境没有分配分支信息！");
        }
        SBranchMapping branchMapping = branchMappings.get(0);
        return branchService.selectById(branchMapping.getGuidBranch());
    }

    @Override
    public List<SBranch> mayRelevanceReleaseBranch() {

        List<SBranchMapping> sBranchMappings = branchMappingService.selectList(null);
        List<Integer> branchGuid = new ArrayList<>();
        if(sBranchMappings.size() > 0) {
            for (SBranchMapping branchMapping : sBranchMappings) {
                branchGuid.add(branchMapping.getGuidBranch());
            }
            if (branchGuid.size() == 0) {
                throw new DeveloperException("没有查询到未被指配的分支");
            }
        }
        EntityWrapper<SBranch> branchEntityWrapper = new EntityWrapper<>();
        branchEntityWrapper.notIn(SBranch.COLUMN_GUID,branchGuid);
        branchEntityWrapper.eq(SBranch.COLUMN_BRANCH_TYPE,BranchType.RELEASE);
        return branchService.selectList(branchEntityWrapper);
    }

    @Override
    public Page<ProfileBranchDetail> profileFullPathDetail(Page<ProfileBranchDetail> page,
                                                           EntityWrapper<ProfileBranchDetail> wrapper) {

        return page.setRecords(this.baseMapper.selectProfileDetail(page,wrapper));
    }


}

