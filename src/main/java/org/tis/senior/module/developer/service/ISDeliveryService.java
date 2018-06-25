package org.tis.senior.module.developer.service;

import com.baomidou.mybatisplus.service.IService;
import org.tis.senior.module.developer.controller.request.MergeDeliveryRequest;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.vo.DeliveryDetail;

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
    void mergeDeliver(MergeDeliveryRequest mergeDelivery, String userId);

    /**
     * 核对合并清单
     * @param profileId
     * @return
     */
    DeliveryDetail check(String profileId);
}

