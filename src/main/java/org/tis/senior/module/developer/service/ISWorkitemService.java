package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.WorkitemAddAndUpdateRequest;
import org.tis.senior.module.developer.controller.request.WorkitemAndBranchAddRequest;
import org.tis.senior.module.developer.controller.request.WorkitemBranchDetailRequest;
import org.tis.senior.module.developer.entity.SBranch;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.entity.vo.WorkitemBranchDetail;

import java.util.List;

/**
 * sWorkitem的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISWorkitemService extends IService<SWorkitem>  {


    /**
     * 查询登录用户的工作项
     *
     * @param userName
     * @return
     */
    List<SWorkitem> selectWorkitemByUser(String userName);

    SWorkitem selectOneById(String workitemId);

    /**
     * 添加工作项以及分支信息
     * @param request
     */
    void insertWorkitemAndBranch(WorkitemAndBranchAddRequest request);

    /**
     * 新增工资项及关联分支
     * @param request
     * @param guidBranch
     */
    void insertWorkitemAndBranchMapping(WorkitemAddAndUpdateRequest request,Integer guidBranch);

    void deleteWorkitemAndBranchMapping(Integer guidWorkitem);

    /**
     *
     * @param guidWorkitem
     * @return
     */
    WorkitemBranchDetailRequest workitemDetail(Integer guidWorkitem);

    /**
     * 工作项关联分支
     *
     * @param guidWorkitem
     * @param guidBranch
     */
    void workitemRelevanceBranch(Integer guidWorkitem, Integer guidBranch);

    /**
     * 取消关联分支
     * @param guidWorkitem
     */
    void workitemCancelBranch(Integer guidWorkitem);

    /**
     * 根本运行环境guid查询分支信息
     * @param workitemGuid
     * @return
     */
    SBranch selectBranchByWorkitemGuid(Integer workitemGuid);

    /**
     * 可关联的分支
     * @return
     */
    List<SBranch> mayRelevanceBranch();

    /**
     * 查询工作项详情及对应的分支路径
     * @param page\
     * @return
     */
    Page<WorkitemBranchDetail> workitemFullPathDetail(Page<WorkitemBranchDetail> page,
                                                      EntityWrapper<WorkitemBranchDetail> wrapper);

    /**
     * 修改工作项状态未已取消
     * @param guidWorkitem
     */
    void updateStatus(Integer guidWorkitem);

    /**
     * 修改工作项状态未已投产
     * @param guidWorkitem
     */
    void updateStatusPutProduct(Integer guidWorkitem);
}

