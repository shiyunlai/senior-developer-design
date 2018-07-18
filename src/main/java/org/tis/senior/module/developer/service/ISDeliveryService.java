package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.DeliveryOutExeclRequest;
import org.tis.senior.module.developer.controller.request.IsPutDeliveryRequest;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;
import org.tis.senior.module.developer.entity.vo.DeliveryProjectDetail;
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
     * 判断此次投放申请的环境以及投放窗口在之前是否有投放申请
     * @param request
     */
    void whetherPutDelivery(IsPutDeliveryRequest request);

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
    List<DeliveryProjectDetail> selectDeliveryListByGuidDelivery(Integer guidDelivery);

    /**
     * 根据投放时间、打包窗口、运行环境获取投放申请
     * @param request
     * @return
     */
    List<SDelivery> selectDeliveryOutExecl(DeliveryOutExeclRequest request);
}

