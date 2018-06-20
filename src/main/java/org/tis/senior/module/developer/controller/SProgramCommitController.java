package org.tis.senior.module.developer.controller;

import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.entity.SProgramCommit;
import org.tis.senior.module.developer.service.ISProgramCommitService;

/**
 * sProgramCommit的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@RestController
@RequestMapping("/sProgramCommit")
public class SProgramCommitController extends BaseController<SProgramCommit>  {

    @Autowired
    private ISProgramCommitService sProgramCommitService;

    @PostMapping("/add")
    public ResultVO add(@RequestBody @Validated SProgramCommit sProgramCommit) {
        sProgramCommitService.insert(sProgramCommit);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated SProgramCommit sProgramCommit) {
        sProgramCommitService.updateById(sProgramCommit);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{id}")
    public ResultVO delete(@PathVariable @NotBlank(message = "id不能为空") String id) {
        sProgramCommitService.deleteById(id);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{id}")
    public ResultVO detail(@PathVariable @NotBlank(message = "id不能为空") String id) {
        SProgramCommit sProgramCommit = sProgramCommitService.selectById(id);
        if (sProgramCommitService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProgramCommit);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SProgramCommit> page) {
        return  ResultVO.success("查询成功", sProgramCommitService.selectPage(getPage(page), getCondition(page)));
    }
    
}

