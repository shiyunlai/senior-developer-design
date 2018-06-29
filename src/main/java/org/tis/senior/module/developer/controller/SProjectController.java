package org.tis.senior.module.developer.controller;

import org.tis.senior.module.core.web.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.service.ISProjectService;

/**
 * sProject的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sProject")
public class SProjectController extends BaseController<SProject>  {

    @Autowired
    private ISProjectService sProjectService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SProject sProject) {
        sProjectService.insert(sProject);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SProject sProject) {
        sProjectService.updateById(sProject);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sProjectService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SProject sProject = sProjectService.selectById(id);
        if (sProjectService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProject);
    }
    
    @PostMapping("/list")
    public ResultVO list() {
        return  ResultVO.success("查询成功", sProjectService.selectProjectAll());
    }
    
}

