package org.tis.senior.module.developer.controller;

import org.tis.senior.module.core.web.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.developer.entity.SBranchMapping;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.developer.service.ISBranchMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 * sBranchMapping的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sBranchMapping")
public class SBranchMappingController extends BaseController<SBranchMapping>  {

    @Autowired
    private ISBranchMappingService sBranchMappingService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SBranchMapping sBranchMapping) {
        sBranchMappingService.insert(sBranchMapping);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SBranchMapping sBranchMapping) {
        sBranchMappingService.updateById(sBranchMapping);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sBranchMappingService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SBranchMapping sBranchMapping = sBranchMappingService.selectById(id);
        if (sBranchMappingService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sBranchMapping);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SBranchMapping> page) {
        return  ResultVO.success("查询成功", sBranchMappingService.selectPage(getPage(page), getCondition(page)));
    }
    
}

