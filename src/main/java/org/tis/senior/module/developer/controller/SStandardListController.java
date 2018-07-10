package org.tis.senior.module.developer.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.entity.SStandardList;
import org.tis.senior.module.developer.service.ISStandardListService;

/**
 * sStandardList的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/07/10
 */
@RestController
@RequestMapping("/sStandardList")
public class SStandardListController extends BaseController<SStandardList>  {

    @Autowired
    private ISStandardListService sStandardListService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SStandardList sStandardList) {
        sStandardListService.insert(sStandardList);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SStandardList sStandardList) {
        sStandardListService.updateById(sStandardList);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sStandardListService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SStandardList sStandardList = sStandardListService.selectById(id);
        if (sStandardListService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sStandardList);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SStandardList> page) {
        return  ResultVO.success("查询成功", sStandardListService.selectPage(getPage(page), getCondition(page)));
    }
    
}

