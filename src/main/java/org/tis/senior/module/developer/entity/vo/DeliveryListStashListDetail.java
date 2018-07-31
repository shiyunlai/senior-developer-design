package org.tis.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryListStashListDetail {

    /**
     * 整理出的清单列表
     */
    private List<DeliveryProjectDetail> deliveryDetail;

    /**
     * 贮藏的清单列表
     */
    private List<DeliveryProjectDetail> stashDetail;
}
