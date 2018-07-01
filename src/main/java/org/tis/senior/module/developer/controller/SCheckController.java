package org.tis.senior.module.developer.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.entity.SCheck;
import org.tis.senior.module.developer.entity.enums.PackTime;
import org.tis.senior.module.developer.entity.vo.CheckResultDetail;
import org.tis.senior.module.developer.service.ISCheckService;
import org.tmatesoft.svn.core.SVNException;

import javax.validation.constraints.NotNull;

/**
 * sCheck的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@RestController
@RequestMapping("/checks")
public class SCheckController extends BaseController<SCheck>  {

    @Autowired
    private ISCheckService sCheckService;

    /**
     * 核对清单
     * @param profileId
     * @param packTiming
     * @return
     */
    @RequiresRoles(value = "rct")
    @PostMapping("/profiles/{profileId}/packTiming/{packTiming}")
    public ResultVO add(@PathVariable @NotBlank(message = "环境ID不能为空") String profileId,
                        @PathVariable @NotNull(message = "打包窗口不能为空") String packTiming) throws SVNException {
        CheckResultDetail detail = sCheckService.check(profileId, PackTime.what(packTiming), getUser().getUserId());
        return ResultVO.success(detail);
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SCheck sCheck) {
        sCheckService.updateById(sCheck);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sCheckService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SCheck sCheck = sCheckService.selectById(id);
        if (sCheckService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sCheck);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SCheck> page) {
        return  ResultVO.success("查询成功", sCheckService.selectPage(getPage(page), getCondition(page)));
    }
    
}

