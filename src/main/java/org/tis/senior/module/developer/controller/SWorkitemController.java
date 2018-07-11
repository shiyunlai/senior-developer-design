package org.tis.senior.module.developer.controller;

import org.springframework.beans.BeanUtils;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.springframework.validation.annotation.Validated;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.developer.entity.enums.ItemStatus;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
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


    @PostMapping("/branch/{guidBranch}")
    public ResultVO add(@PathVariable @NotNull(message = "guid不能为空") Integer guidBranch,
            @RequestBody @Validated({WorkitemAddAndUpdateRequest.add.class, Default.class}) WorkitemAddAndUpdateRequest request) {
        sWorkitemService.insertWorkitemAndBranchMapping(request,guidBranch);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated({WorkitemAddAndUpdateRequest.update.class}) WorkitemAddAndUpdateRequest request) {
        SWorkitem sWorkitem = new SWorkitem();
        BeanUtils.copyProperties(request,sWorkitem);
        sWorkitemService.updateById(sWorkitem);
        return ResultVO.success("修改成功！");
    }
    
    @DeleteMapping("/{guid}")
    public ResultVO delete(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        sWorkitemService.deleteById(guid);
        return ResultVO.success("删除成功");
    }
    
    @GetMapping("/{guid}")
    public ResultVO detail(@PathVariable @NotNull(message = "id不能为空") Integer guid) {
        SWorkitem sWorkitem = sWorkitemService.selectById(guid);
        if (sWorkitemService == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", sWorkitem);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<SWorkitem> page) {
        return  ResultVO.success("查询成功", sWorkitemService.selectPage(getPage(page), getCondition(page)));
    }

    /**
     * 查询登录用户的工作项
     *
     * @return
     */
    @GetMapping
    public ResultVO loadLoginUserWork(){

        SSvnAccount sSvnAccount = getUser();
        List<SWorkitem> swList = sWorkitemService.selectWorkitemByUser(sSvnAccount.getUserId());

        return ResultVO.success("查询成功",swList);
    }

    /**
     * 查询工作项的开发分支
     *
     * @return
     */
    @GetMapping("/{workitemGuid}/branchDetail")
    public ResultVO loadBranchDetail(@PathVariable @NotBlank(message = "工作项id不能为空")String workitemGuid) throws Exception {

        SBranch branch = sWorkitemService.selectBranchByWorkitemId(workitemGuid);
        return ResultVO.success("查询成功",branch);
    }

    /**
     * 修改工作项状态为已取消
     * @return
     */
    @PutMapping("/{workitemGuid}/status")
    public ResultVO updateItemStatus(@PathVariable @NotBlank(message = "工作项id不能为空")String workitemGuid){
        SWorkitem workitem = sWorkitemService.selectById(workitemGuid);
        workitem.setItemStatus(ItemStatus.CANCEL);
        sWorkitemService.updateById(workitem);
        return ResultVO.success("修改成功！");
    }
    
}

