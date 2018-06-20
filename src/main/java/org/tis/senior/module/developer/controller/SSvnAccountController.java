package org.tis.senior.module.developer.controller;

import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.service.ISSvnAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.entity.SSvnAccount;

/**
 * sSvnAccount的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sSvnAccount")
public class SSvnAccountController extends BaseController<SSvnAccount>  {

    @Autowired
    private ISSvnAccountService sSvnAccountService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SSvnAccount sSvnAccount) {
        sSvnAccountService.insert(sSvnAccount);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SSvnAccount sSvnAccount) {
        sSvnAccountService.updateById(sSvnAccount);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sSvnAccountService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SSvnAccount sSvnAccount = sSvnAccountService.selectById(id);
        if (sSvnAccountService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sSvnAccount);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SSvnAccount> page) {
        return  ResultVO.success("查询成功", sSvnAccountService.selectPage(getPage(page), getCondition(page)));
    }
    
}

