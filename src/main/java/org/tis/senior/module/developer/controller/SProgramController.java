package org.tis.senior.module.developer.controller;

import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.entity.SProgram;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.service.ISProgramService;

/**
 * sProgram的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sProgram")
public class SProgramController extends BaseController<SProgram>  {

    @Autowired
    private ISProgramService sProgramService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SProgram sProgram) {
        sProgramService.insert(sProgram);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SProgram sProgram) {
        sProgramService.updateById(sProgram);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sProgramService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SProgram sProgram = sProgramService.selectById(id);
        if (sProgramService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProgram);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SProgram> page) {
        return  ResultVO.success("查询成功", sProgramService.selectPage(getPage(page), getCondition(page)));
    }
    
}

