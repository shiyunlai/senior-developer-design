package org.tis.senior.module.developer.controller;

import org.tis.senior.module.developer.entity.SDeliveryList;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.developer.service.ISDeliveryListService;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;

/**
 * sDeliveryList的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sDeliveryList")
public class SDeliveryListController extends BaseController<SDeliveryList>  {

    @Autowired
    private ISDeliveryListService sDeliveryListService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SDeliveryList sDeliveryList) {
        sDeliveryListService.insert(sDeliveryList);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SDeliveryList sDeliveryList) {
        sDeliveryListService.updateById(sDeliveryList);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sDeliveryListService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SDeliveryList sDeliveryList = sDeliveryListService.selectById(id);
        if (sDeliveryListService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sDeliveryList);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SDeliveryList> page) {
        return  ResultVO.success("查询成功", sDeliveryListService.selectPage(getPage(page), getCondition(page)));
    }
    
}

