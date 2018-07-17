package org.tis.senior.module.developer.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.ProfileAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.ProfileUpdateStatusRequest;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;
import org.tis.senior.module.developer.entity.vo.ProfileBranchDetail;
import org.tis.senior.module.developer.service.ISProfilesService;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * sProfiles的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sProfiles")
@Validated
public class SProfilesController extends BaseController<SProfiles>  {

    @Autowired
    private ISProfilesService sProfilesService;

    @PostMapping
    public ResultVO add(@RequestBody @Validated({ProfileAddAndUpdateRequest.add.class, Default.class}) ProfileAddAndUpdateRequest request) {
        SProfiles sProfiles = new SProfiles();
        BeanUtils.copyProperties(request,sProfiles);
        sProfiles.setProfilesCode(sProfiles.getProfilesName());
        sProfiles.setIsAllowDelivery(IsAllowDelivery.ALLOW);
        sProfilesService.insert(sProfiles);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated({ProfileAddAndUpdateRequest.update.class, Default.class}) ProfileAddAndUpdateRequest request) {
        SProfiles sProfiles = new SProfiles();
        BeanUtils.copyProperties(request,sProfiles);
        sProfiles.setProfilesCode(sProfiles.getProfilesName());
        sProfilesService.updateById(sProfiles);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{guid}")
    public ResultVO delete(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        sProfilesService.deleteById(guid);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{guid}")
    public ResultVO detail(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        SProfiles sProfiles = sProfilesService.selectById(guid);
        if (sProfilesService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProfiles);
    }

    /**
     * 查询所有运行环境
     *
     * @return
     */
    @GetMapping("/list")
    public ResultVO list() {
        return  ResultVO.success("查询成功", sProfilesService.selectProfilesAll());
    }

    /**
     * 查询所有运行环境及分页
     * @param page
     * @return
     */
    @PostMapping("/all")
    public ResultVO profielAll(@RequestBody @Validated SmartPage<ProfileBranchDetail> page){

        Page<ProfileBranchDetail> workitemDetailPage = new Page<ProfileBranchDetail>
                (page.getPage().getCurrent(), page.getPage().getSize(),
                        page.getPage().getOrderByField(), page.getPage().getAsc());

        return ResultVO.success("查询成功",
                sProfilesService.profileFullPathDetail(workitemDetailPage, getWrapper(page.getCondition())));
    }

    /**
     * 修改运行环境状态
     * @param request
     */
    @PutMapping("/status")
    public ResultVO updateStatus(@RequestBody @Validated ProfileUpdateStatusRequest request){
        sProfilesService.updateProfileStatus(request.getGuid(), request.getIsAllowDelivery());
        return ResultVO.success("修改成功！");
    }

    /**
     * 关联分支
     * @param profileGuid
     * @param guidBranch
     * @return
     */
    @GetMapping("/{profileGuid}/branch/{guidBranch}")
    public ResultVO relevanceBranch(@PathVariable @NotNull(message = "运行环境guid不能为空")Integer profileGuid,
                                    @PathVariable @NotNull(message = "分支guid不能为空")Integer guidBranch){
        sProfilesService.profileRelevanceBranch(profileGuid,guidBranch);
        return ResultVO.success("关联成功！");
    }

    /**
     * 取消关联分支
     * @param profileGuid
     * @return
     */
    @GetMapping("/{profileGuid}/cancel")
    public ResultVO cancelBranch(@PathVariable @NotNull(message = "运行环境id不能为空")Integer profileGuid){
        sProfilesService.profileCancelBranch(profileGuid);
        return ResultVO.success("取消分支成功！");
    }

    /**
     * 根本运行环境guid查询分支信息
     * @param profileGuid
     * @return
     */
    @GetMapping("/{profileGuid}/branchDetail")
    public ResultVO selectBranchDetail(@PathVariable @NotNull(message = "运行环境id不能为空")Integer profileGuid){

        return ResultVO.success("查询成功！",sProfilesService.selectBranchByProfileGuid(profileGuid));
    }

    /**
     * 可关联分支
     * @return
     */
    @GetMapping("/relevanceBranch")
    public ResultVO relevanceBranch(){
        return ResultVO.success("查询成功",sProfilesService.mayRelevanceReleaseBranch());
    }
}

