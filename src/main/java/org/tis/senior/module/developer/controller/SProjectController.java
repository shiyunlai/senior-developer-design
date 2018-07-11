package org.tis.senior.module.developer.controller;

import org.springframework.beans.BeanUtils;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.developer.controller.request.BranchAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.ProjectAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.service.ISProjectService;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * sProject的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sProject")
@Validated
public class SProjectController extends BaseController<SProject>  {

    @Autowired
    private ISProjectService sProjectService;

    @PostMapping
    public ResultVO add(@RequestBody @Validated({ProjectAddAndUpdateRequest.add.class,Default.class})ProjectAddAndUpdateRequest request) {
        SProject sProject = new SProject();
        BeanUtils.copyProperties(request,sProject);
        sProjectService.insert(sProject);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated({ProjectAddAndUpdateRequest.update.class, Default.class})ProjectAddAndUpdateRequest request) {
        SProject sProject = new SProject();
        BeanUtils.copyProperties(request,sProject);
        sProjectService.updateById(sProject);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{guid}")
    public ResultVO delete(@PathVariable @NotNull(message = "guid不能为空") Integer guid) {
        sProjectService.deleteById(guid);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{guid}")
    public ResultVO detail(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        SProject sProject = sProjectService.selectById(guid);
        if (sProjectService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sProject);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SProject> page) {

        return  ResultVO.success("查询成功", sProjectService.selectPage(getPage(page), getCondition(page)));

    }
    
}

