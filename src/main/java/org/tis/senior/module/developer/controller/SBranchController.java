package org.tis.senior.module.developer.controller;

import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.service.ISBranchService;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;

/**
 * sBranch的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sBranch")
public class SBranchController extends BaseController<SBranch>  {

    @Autowired
    private ISBranchService sBranchService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SBranch sBranch) {
        sBranchService.insert(sBranch);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SBranch sBranch) {
        sBranchService.updateById(sBranch);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sBranchService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SBranch sBranch = sBranchService.selectById(id);
        if (sBranchService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sBranch);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SBranch> page) {
        return  ResultVO.success("查询成功", sBranchService.selectPage(getPage(page), getCondition(page)));
    }
    
}

