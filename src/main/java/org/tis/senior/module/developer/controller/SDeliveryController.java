package org.tis.senior.module.developer.controller;

import org.tis.senior.module.core.web.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.service.ISDeliveryService;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 * sDelivery的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sDelivery")
public class SDeliveryController extends BaseController<SDelivery>  {

    @Autowired
    private ISDeliveryService sDeliveryService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SDelivery sDelivery) {
        sDeliveryService.insert(sDelivery);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SDelivery sDelivery) {
        sDeliveryService.updateById(sDelivery);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sDeliveryService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SDelivery sDelivery = sDeliveryService.selectById(id);
        if (sDeliveryService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sDelivery);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SDelivery> page) {
        return  ResultVO.success("查询成功", sDeliveryService.selectPage(getPage(page), getCondition(page)));
    }
    
}

