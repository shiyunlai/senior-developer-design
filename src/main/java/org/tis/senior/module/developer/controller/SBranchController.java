package org.tis.senior.module.developer.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.BranchAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.service.ISBranchService;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * sBranch的Controller类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@RestController
@RequestMapping("/sBranch")
@Validated
public class SBranchController extends BaseController<SBranch>  {

    @Autowired
    private ISBranchService sBranchService;

    @PostMapping
    public ResultVO add(@RequestBody @Validated({BranchAddAndUpdateRequest.add.class,Default.class})BranchAddAndUpdateRequest request) {
        SSvnAccount user = getUser();
        SBranch sBranch = new SBranch();
        BeanUtils.copyProperties(request,sBranch);
        sBranch.setCreater(user.getUserId());
        sBranch.setCreateTime(new Date());
        sBranch.setCurrVersion(request.getLastVersion());
        sBranchService.insert(sBranch);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated({BranchAddAndUpdateRequest.update.class,Default.class})BranchAddAndUpdateRequest request) {
        SBranch sBranch = new SBranch();
        BeanUtils.copyProperties(request,sBranch);
        sBranchService.updateById(sBranch);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{guid}")
    public ResultVO delete(@PathVariable @NotNull(message = "guid不能为空") Integer guid) {
        sBranchService.deleteBranchAndMapping(guid);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{guid}")
    public ResultVO detail(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        SBranch sBranch = sBranchService.selectById(guid);
        if (sBranchService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sBranch);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SBranch> page) {
        return  ResultVO.success("查询成功", sBranchService.selectPage(getPage(page), getCondition(page)));
    }

    @GetMapping("/notAllot")
    public ResultVO notAllot() {
        return  ResultVO.success("查询成功", sBranchService.selectNotAllotBranch());
    }

    /**
     * 验证url是否是合法的svn路径
     * @param svnUrl
     * @return
     */
    @GetMapping("/path/{svnUrl}/revision")
    public ResultVO verificationUrl(@PathVariable @NotBlank(message = "svnUrl不能为空") String svnUrl){

        return  ResultVO.success("验证通过", sBranchService.verificationUrl(svnUrl));
    }
}

