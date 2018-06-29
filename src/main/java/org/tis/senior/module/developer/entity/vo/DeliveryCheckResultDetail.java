package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SWorkitem;

import java.util.List;

/**
 * description: 核对结果
 *
 * @author zhaoch
 * @date 2018/6/28
 **/
@Data
public class DeliveryCheckResultDetail {

    /**
     * 工作项信息
     */
    private SWorkitem workitem;

    /**
     * 投放信息
     */
    private SDelivery delivery;

    /**
     * 投放清单
     */
    private List<DeliveryProjectDetail> detailList;
}
