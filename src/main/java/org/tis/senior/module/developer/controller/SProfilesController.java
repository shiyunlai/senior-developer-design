package org.tis.senior.module.developer.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.ProfileAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;
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
    public ResultVO profielAll(@RequestBody @Validated SmartPage<SProfiles> page){
        return ResultVO.success("查询成功",sProfilesService.selectPage(getPage(page), getCondition(page)));
    }

    /**
     * 修改运行环境状态为不允许投放
     * @param profileGuid
     */
    @PutMapping("/{profileGuid}/status")
    public ResultVO updateStatus(@PathVariable @NotNull(message = "id不能为空") Integer profileGuid){
        sProfilesService.updateProfileStatus(profileGuid);
        return ResultVO.success("修改成功！");
    }
}

