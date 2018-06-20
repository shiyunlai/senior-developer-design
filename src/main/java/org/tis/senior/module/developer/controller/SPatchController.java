package org.tis.senior.module.developer.controller;

import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.entity.SPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.service.ISPatchService;

/**
 * sPatch的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sPatch")
public class SPatchController extends BaseController<SPatch>  {

    @Autowired
    private ISPatchService sPatchService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SPatch sPatch) {
        sPatchService.insert(sPatch);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SPatch sPatch) {
        sPatchService.updateById(sPatch);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sPatchService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SPatch sPatch = sPatchService.selectById(id);
        if (sPatchService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sPatch);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SPatch> page) {
        return  ResultVO.success("查询成功", sPatchService.selectPage(getPage(page), getCondition(page)));
    }
    
}

