package org.tis.senior.module.developer.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.service.ISDeliveryService;

/**
 * sDelivery的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/deliveries")
@Validated
public class SDeliveryController extends BaseController<SDelivery>  {

    @Autowired
    private ISDeliveryService sDeliveryService;

    @PostMapping
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

    /**
     * 获取合并投放清单信息
     * @param request
     * @return
     */
    @PostMapping("/merge/info")
    public ResultVO mergeInfo(@RequestBody @Validated MergeDeliveryRequest request) {
        return ResultVO.success(sDeliveryService.getMergeInfo(request, getUser().getUserId()));
    }


    /**
     * 合并投放（开发人员使用）
     * 1、选择多条“投放申请”，合并为一个新的投放申请；
     * 2、合并的投放申请都是成功的；
     * 3、合并的投放申请都来自同一个运行环境；
     * @return
     */
    @PostMapping("/merge")
    public ResultVO merge(@RequestBody @Validated MergeDeliveryRequest request) {
        sDeliveryService.mergeDeliver(request, getUser().getUserId());
        return ResultVO.success("申请合并投放成功！");
    }

    /**
     * 查询一条投放申请中的工程名
     * @param guidDelivery
     * @return
     */
    @GetMapping("/{guidDelivery}/projectName")
    public ResultVO projectName(@PathVariable @NotBlank(message = "id不能为空") String guidDelivery){

        return ResultVO.success("查询成功",sDeliveryService.selectDeliveryProName(guidDelivery));
    }


    
}

