package org.tis.senior.module.developer.controller;

import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.entity.SProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.service.ISProfilesService;

/**
 * sProfiles的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sProfiles")
public class SProfilesController extends BaseController<SProfiles>  {

    @Autowired
    private ISProfilesService sProfilesService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SProfiles sProfiles) {
        sProfilesService.insert(sProfiles);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SProfiles sProfiles) {
        sProfilesService.updateById(sProfiles);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sProfilesService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SProfiles sProfiles = sProfilesService.selectById(id);
        if (sProfilesService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProfiles);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SProfiles> page) {
        return  ResultVO.success("查询成功", sProfilesService.selectPage(getPage(page), getCondition(page)));
    }
    
}

