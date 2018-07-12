package org.tis.senior.module.developer.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;
import org.tis.senior.module.developer.entity.enums.BranchMappingStatus;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.tis.senior.module.developer.service.ISBranchService;
import org.tis.senior.module.developer.service.ISProfilesService;

import javax.annotation.PostConstruct;
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

    @Override
    public List<SProfiles> selectProfilesAll() {
        return this.spList;
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
            throw new DeveloperException("次分支已被指配，请重新选择分支！");
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
            throw new DeveloperException("此运行环境没有分配分支！");
        }
        SBranchMapping branchMapping = sbmList.get(0);
        branchMappingService.deleteById(branchMapping.getGuid());
    }

    /**
     * 查询所有的运行环境
     */
    @PostConstruct
    public void selectAll(){
        EntityWrapper<SProfiles> spEntityWrapper = new EntityWrapper<>();
        spEntityWrapper.eq(SProfiles.COLUMN_IS_ALLOW_DELIVERY,IsAllowDelivery.ALLOW);
        this.spList = selectList(spEntityWrapper);
    }
}

