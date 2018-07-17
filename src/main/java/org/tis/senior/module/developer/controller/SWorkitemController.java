package org.tis.senior.module.developer.controller;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.enums.ItemStatus;
import org.tis.senior.module.developer.entity.vo.WorkitemBranchDetail;
import org.tis.senior.module.developer.service.ISWorkitemService;

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
@Validated
public class SWorkitemController extends BaseController<SWorkitem>  {

    @Autowired
    private ISWorkitemService sWorkitemService;


    @PostMapping
    public ResultVO add(@RequestBody @Validated({WorkitemAddAndUpdateRequest.add.class, Default.class})
                                    WorkitemAddAndUpdateRequest request) {
        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request,workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        sWorkitemService.insert(workitem);
        return ResultVO.success("新增成功！");
    }
    
    @PutMapping
    public ResultVO update(@RequestBody @Validated({WorkitemAddAndUpdateRequest.update.class, Default.class})
                                       WorkitemAddAndUpdateRequest request) {
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
    
    @GetMapping("/{guidWorkitem}")
    public ResultVO detail(@PathVariable @NotNull(message = "id不能为空") Integer guidWorkitem) {
        SWorkitem workitem = sWorkitemService.selectById(guidWorkitem);
        if (workitem == null) {
            return ResultVO.error("404", "找不到对应记录或已经被删除！");
        }
        return ResultVO.success("查询成功", workitem);
    }
    
    @PostMapping("/list")
    public ResultVO list(@RequestBody @Validated SmartPage<WorkitemBranchDetail> page) {

        Page<WorkitemBranchDetail> workitemDetailPage = new Page<WorkitemBranchDetail>
                (page.getPage().getCurrent(), page.getPage().getSize(),
                page.getPage().getOrderByField(), page.getPage().getAsc());

        return  ResultVO.success("查询成功",
                sWorkitemService.workitemFullPathDetail(workitemDetailPage,getWrapper(page.getCondition())));
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
     * 修改工作项状态为已取消
     * @return
     */
    @PutMapping("/{workitemGuid}/status")
    public ResultVO updateItemStatus(@PathVariable @NotNull(message = "工作项id不能为空")Integer workitemGuid){
        SWorkitem workitem = sWorkitemService.selectById(workitemGuid);
        workitem.setItemStatus(ItemStatus.CANCEL);
        sWorkitemService.updateById(workitem);
        return ResultVO.success("修改成功！");
    }


    /**
     * 关联分支
     * @param workitemGuid
     * @param guidBranch
     * @return
     */
    @GetMapping("/{workitemGuid}/branch/{guidBranch}")
    public ResultVO relevanceBranch(@PathVariable @NotNull(message = "工作项guid不能为空")Integer workitemGuid,
                                    @PathVariable @NotNull(message = "分支guid不能为空")Integer guidBranch){
        sWorkitemService.workitemRelevanceBranch(workitemGuid,guidBranch);
        return ResultVO.success("关联成功！");
    }

    /**
     * 取消关联分支
     * @param workitemGuid
     * @return
     */
    @GetMapping("/{workitemGuid}/cancel")
    public ResultVO cancelBranch(@PathVariable @NotNull(message = "工作项id不能为空")Integer workitemGuid){
        sWorkitemService.workitemCancelBranch(workitemGuid);
        return ResultVO.success("取消分支成功！");
    }

    /**
     * 根据运行环境guid查询分支信息
     * @param workitemGuid
     * @return
     */
    @GetMapping("/{workitemGuid}/branchDetail")
    public ResultVO selectBranchDetail(@PathVariable @NotNull(message = "运行环境id不能为空")Integer workitemGuid){

        return ResultVO.success("查询成功！",sWorkitemService.selectBranchByWorkitemGuid(workitemGuid));
    }

    /**
     * 可关联分支
     * @return
     */
    @GetMapping("/relevanceBranch")
    public ResultVO relevanceBranch(){
        return ResultVO.success("查询成功",sWorkitemService.mayRelevanceBranch());
    }


}

