package org.tis.senior.module.developer.controller;

import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * sWorkitem的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sWorkitem")
public class SWorkitemController extends BaseController<SWorkitem>  {

    @Autowired
    private ISWorkitemService sWorkitemService;


    @PostMapping
    public ResultVO add(@RequestBody @Validated SWorkitem sWorkitem) {
        sWorkitemService.insert(sWorkitem);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SWorkitem sWorkitem) {
        sWorkitemService.updateById(sWorkitem);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sWorkitemService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SWorkitem sWorkitem = sWorkitemService.selectById(id);
        if (sWorkitemService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sWorkitem);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SWorkitem> page) {
        return  ResultVO.success("查询成功", sWorkitemService.selectPage(getPage(page), getCondition(page)));
    }

    @GetMapping
    public ResultVO loadLoginUserWork(){

        SSvnAccount sSvnAccount = getUser();
        List<SWorkitem> swList = sWorkitemService.selectWorkitemByUser(sSvnAccount.getUserId());

        return ResultVO.success("查询成功",swList);
    }
    
}

