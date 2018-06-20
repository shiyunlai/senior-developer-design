package org.tis.senior.module.developer.controller;

import org.tis.senior.module.core.web.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.developer.entity.SNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.developer.service.ISNoticeService;
import org.hibernate.validator.constraints.NotBlank;

/**
 * sNotice的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sNotice")
public class SNoticeController extends BaseController<SNotice>  {

    @Autowired
    private ISNoticeService sNoticeService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SNotice sNotice) {
        sNoticeService.insert(sNotice);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SNotice sNotice) {
        sNoticeService.updateById(sNotice);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sNoticeService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SNotice sNotice = sNoticeService.selectById(id);
        if (sNoticeService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sNotice);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SNotice> page) {
        return  ResultVO.success("查询成功", sNoticeService.selectPage(getPage(page), getCondition(page)));
    }
    
}

