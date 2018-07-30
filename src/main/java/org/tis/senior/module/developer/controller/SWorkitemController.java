package org.tis.senior.module.developer.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.baomidou.mybatisplus.plugins.Page;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.controller.BaseController;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.controller.request.WorkItemAddBranchRequest;
import org.tis.senior.module.developer.controller.request.WorkItemAddProjectRequest;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.enums.BranchType;
import org.tis.senior.module.developer.entity.enums.ItemStatus;
import org.tis.senior.module.developer.entity.vo.ProjectDetail;
import org.tis.senior.module.developer.entity.vo.WorkitemBranchDetail;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.tmatesoft.svn.core.SVNException;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
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
public class SWorkitemController extends BaseController<SWorkitem> {

    @Autowired
    private ISWorkitemService sWorkitemService;


    @PostMapping
    public ResultVO add(@RequestBody @Validated({WorkitemAddAndUpdateRequest.add.class, Default.class})
                                WorkitemAddAndUpdateRequest request) {
        SWorkitem workitem = new SWorkitem();
        BeanUtils.copyProperties(request, workitem);
        workitem.setItemStatus(ItemStatus.DEVELOP);
        sWorkitemService.insert(workitem);
        return ResultVO.success("新增成功！");
    }

    @PutMapping
    public ResultVO update(@RequestBody @Validated({WorkitemAddAndUpdateRequest.update.class, Default.class})
                                   WorkitemAddAndUpdateRequest request) {
        SWorkitem sWorkitem = new SWorkitem();
        BeanUtils.copyProperties(request, sWorkitem);
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

        return ResultVO.success("查询成功",
                sWorkitemService.workitemFullPathDetail(workitemDetailPage, getWrapper(page.getCondition())));
    }

    /**
     * 查询登录用户的工作项
     *
     * @return
     */
    @GetMapping
    public ResultVO loadLoginUserWork() {

        SSvnAccount sSvnAccount = getUser();
        List<SWorkitem> swList = sWorkitemService.selectWorkitemByUser(sSvnAccount.getUserId());

        return ResultVO.success("查询成功", swList);
    }

    /**
     * 修改工作项状态为已取消
     *
     * @return
     */
    @PutMapping("/{workitemGuid}/status")
    public ResultVO updateItemStatus(@PathVariable @NotNull(message = "工作项id不能为空") Integer workitemGuid) {

        sWorkitemService.updateStatus(workitemGuid);
        return ResultVO.success("修改成功！");
    }

    /**
     * 修改工作项状态为已投产
     *
     * @return
     */
    @PutMapping("/{workitemGuid}/putProductStatus")
    public ResultVO updateStatusPutProduct(@PathVariable @NotNull(message = "工作项id不能为空") Integer workitemGuid) {

        sWorkitemService.updateStatusPutProduct(workitemGuid);
        return ResultVO.success("修改成功!");
    }


    /**
     * 关联分支
     *
     * @param workitemGuid
     * @param guidBranch
     * @return
     */
    @GetMapping("/{workitemGuid}/branch/{guidBranch}")
    public ResultVO relevanceBranch(@PathVariable @NotNull(message = "工作项guid不能为空") Integer workitemGuid,
                                    @PathVariable @NotNull(message = "分支guid不能为空") Integer guidBranch) {
        sWorkitemService.workitemRelevanceBranch(workitemGuid, guidBranch);
        return ResultVO.success("关联成功！");
    }

    /**
     * 取消关联分支
     *
     * @param workitemGuid
     * @return
     */
    @GetMapping("/{workitemGuid}/cancel")
    public ResultVO cancelBranch(@PathVariable @NotNull(message = "工作项id不能为空") Integer workitemGuid) {
        sWorkitemService.workitemCancelBranch(workitemGuid);
        return ResultVO.success("取消分支成功！");
    }

    /**
     * 根据运行环境guid查询分支信息
     *
     * @param workitemGuid
     * @return
     */
    @GetMapping("/{workitemGuid}/branchDetail")
    public ResultVO selectBranchDetail(@PathVariable @NotNull(message = "运行环境id不能为空") Integer workitemGuid) {

        return ResultVO.success("查询成功！", sWorkitemService.selectBranchByWorkitemGuid(workitemGuid));
    }

    /**
     * 可关联分支
     *
     * @return
     */
    @GetMapping("/relevanceBranch")
    public ResultVO relevanceBranch() {
        return ResultVO.success("查询成功", sWorkitemService.mayRelevanceBranch());
    }

    /**
     * 新增工作项分支
     * @param guid
     * @param request
     * @return
     * @throws SVNException
     */
    @PostMapping("/{guid}/branch")
    public ResultVO addBranch(@PathVariable @NotBlank(message = "工作项id不能为空") String guid,
                              @RequestBody @Validated WorkItemAddBranchRequest request) throws SVNException {
        if (request.getBranchType().equals(BranchType.RELEASE)) {
            throw new DeveloperException("工作项不能创建RELEASE分支！");
        }
        SBranch sBranch = new SBranch();
        sBranch.setBranchFor(request.getBranchFor());
        sBranch.setBranchType(request.getBranchType());
        sBranch.setCreater(getUser().getUserId());
        sBranch.setCreateTime(new Date());
        sWorkitemService.insertBranch(guid, sBranch);
        return ResultVO.success("新增工作项分支成功！");
    }

    /**
     * 获取工作项的工程详情
     * @param guid
     * @return
     */
    @FastJsonView(exclude = {@FastJsonFilter(clazz = SProject.class, props = {"deployConfig"})})
    @GetMapping("/{guid}/project")
    public ResultVO selectProject(@PathVariable @NotBlank(message = "工作项id不能为空") String guid) throws SVNException {
        ProjectDetail result = sWorkitemService.selectProjects(guid);
        return ResultVO.success("查询成功！", result);
    }

    /**
     * 拉工程
     * @param guid
     * @param request
     * @return
     * @throws SVNException
     */
    @PostMapping("/{guid}/project")
    public ResultVO addProject(@PathVariable @NotBlank(message = "工作项id不能为空") String guid,
                              @RequestBody @Validated WorkItemAddProjectRequest request) throws SVNException {
        sWorkitemService.insertProjects(guid, request.getMessage(), request.getProjectGuids());
        return ResultVO.success("拉取工程成功！");
    }
}

