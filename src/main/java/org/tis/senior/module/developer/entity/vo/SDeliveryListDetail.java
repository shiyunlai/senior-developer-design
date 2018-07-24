package org.tis.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class SDeliveryListDetail {

    /**
     * 投放的代码清单总条数
     */
    private Integer count;

    /**
     * 代码清单
     */
    private List<DeliveryProjectDetail> deliveryProjectDetails;

    /**
     * 分支路径
     */
    private String fullPath;
}
