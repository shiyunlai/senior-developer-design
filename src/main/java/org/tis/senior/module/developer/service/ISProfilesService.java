package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.ProfileAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.ProfileAndBranchAddRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;
import org.tis.senior.module.developer.entity.vo.ProfileBranchDetail;
import org.tis.senior.module.developer.entity.vo.SProfileDetail;

import java.text.ParseException;
import java.util.List;

/**
 * sProfiles的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISProfilesService extends IService<SProfiles>  {

    /**
     * 查询所有运行环境
     * @return
     */
    List<SProfiles> selectProfilesAll();

    SProfiles selectOneById(String guidProfile);

    /**
     * 新增运行环境和分支
     * @param request
     */
    void insertProfileAndBranch(ProfileAndBranchAddRequest request);

    /**
     * 删除运行环境
     * @param profileGuid
     */
    void deleteProfileAndBranchMapping(Integer profileGuid);

    /**
     * 修改运行环境状态
     * @param profileGuid
     */
    void updateProfileStatus(Integer profileGuid, IsAllowDelivery isAllowDelivery);

    /**
     * 新增运行环境及分支关联表
     * @param request
     * @param guidBranch
     */
    void insertProfileBranchMapping(ProfileAddAndUpdateRequest request,Integer guidBranch);

    /**
     * 运行环境关联分支
     *
     * @param guidProfile
     * @param guidBranch
     */
    void profileRelevanceBranch(Integer guidProfile, Integer guidBranch);

    /**
     * 取消关联分支
     * @param guidProfile
     */
    void profileCancelBranch(Integer guidProfile);

    /**
     * 根本运行环境guid查询分支信息
     * @param profileGuid
     * @return
     */
    SBranch selectBranchByProfileGuid(Integer profileGuid);

    /**
     * 查询可关联的Release分支
     * @return
     */
    List<SBranch> mayRelevanceReleaseBranch();

    /**
     * 查询运行环境详情及对应的分支路径
     * @param page
     * @param wrapper
     * @return
     */
    Page<ProfileBranchDetail> profileFullPathDetail(Page<ProfileBranchDetail> page,
                                                     EntityWrapper<ProfileBranchDetail> wrapper);

    /**
     * 校验环境,正确返回环境信息
     * @param profileId 环境ID
     * @param packTiming 打包窗口
     * @return
     */
    SProfiles validateProfiles(String profileId, String packTiming);

    /**
     * 查询所有运行环境同时验证窗口
     * @return
     */
    List<SProfileDetail> profileAllPackTimeVerify() throws ParseException;
}

