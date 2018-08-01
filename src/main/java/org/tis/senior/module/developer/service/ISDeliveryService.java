package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.DeliveredNewProfilesRequest;
import org.tis.senior.module.developer.controller.request.DeliveryOutExeclRequest;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.controller.request.SDeliveryUpdateRequest;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;
import org.tis.senior.module.developer.entity.vo.SDeliveryListDetail;
import org.tis.senior.module.developer.entity.vo.SProfileDetail;
import org.tmatesoft.svn.core.SVNException;

import java.text.ParseException;
import java.util.List;

/**
 * sDelivery的Service接口类
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
public interface ISDeliveryService extends IService<SDelivery>  {


    Page<SDelivery> getDeliveryAll(Page<SDelivery> page,
                                   EntityWrapper<SDelivery> wrapper,SSvnAccount sSvnAccount);

    /**
     * 获取合并投放信息
     * @param mergeDelivery
     * @param userId
     * @return
     */
     DeliveryDetail getMergeInfo(MergeDeliveryRequest mergeDelivery, String userId);

    /**
     * 合并投放申请
     * @param mergeDelivery
     * @param userId
     */
    void mergeDelivery(MergeDeliveryRequest mergeDelivery, String userId);

    /**
     * 查询一条投放申请中的工程名
     * @param guidDelivery
     * @return
     */
    List<String> selectDeliveryProName(String guidDelivery);

    /**
     * 查询工作项所要追加的投放申请集合
     * @param workitemGuid
     * @return
     */
    List<SDelivery> selectAddToDelivery(Integer workitemGuid) throws ParseException;

    /**
     * 确认合并投放申请
     * @param id 投放申请GUID
     */
    void merge(String id);

    /**
     * 删除投放申请及对应的投放代码
     * @param guidDelivery
     */
    void deleteDeliveryAndDeliveryList(Integer guidDelivery) throws SVNException;

    /**
     * 根据投放申请guid查询投放清单代码集合
     *
     * @param guidDelivery 投放申请的guid
     * @return
     */
    SDeliveryListDetail selectDeliveryListByGuidDelivery(Integer guidDelivery);

    /**
     * 根据投放时间、打包窗口、运行环境获取投放申请
     * @param request
     * @return
     */
    List<SDelivery> selectDeliveryOutExecl(DeliveryOutExeclRequest request);

    /**
     * 修改清单的投放时间及打包窗口
     * @param request
     */
    void updateDelivery(SDeliveryUpdateRequest request) throws ParseException;

    /**
     * 根据guid查询投放申请对应的运行环境并验证每个窗口
     * @return
     */
    SProfileDetail selectProfileDeteilVerify(Integer guidDelivery) throws ParseException;

    /**
     * 复制一个申请投放到新环境
     */
    List<SDelivery> deliveredNewProfiles(DeliveredNewProfilesRequest request);

}

