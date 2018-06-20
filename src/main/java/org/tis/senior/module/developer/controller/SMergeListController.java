package org.tis.senior.module.developer.controller;

import org.tis.senior.module.developer.entity.SMergeList;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.service.ISMergeListService;

/**
 * sMergeList的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sMergeList")
public class SMergeListController extends BaseController<SMergeList>  {

    @Autowired
    private ISMergeListService sMergeListService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SMergeList sMergeList) {
        sMergeListService.insert(sMergeList);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SMergeList sMergeList) {
        sMergeListService.updateById(sMergeList);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sMergeListService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SMergeList sMergeList = sMergeListService.selectById(id);
        if (sMergeListService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sMergeList);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SMergeList> page) {
        return  ResultVO.success("查询成功", sMergeListService.selectPage(getPage(page), getCondition(page)));
    }
    
}

