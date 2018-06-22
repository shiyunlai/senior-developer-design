package org.tis.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description:投放明细
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class DeliveryDetail  {

    /**
     * 工作项信息
     */
    private List<String> workitemNames;

    /**
     * 分支信息
     */
    private List<String> branchs;

    /**
     * 投放小计
     */
    private Map<String, Long> patchCount;

    /**
     * 投放合计
     */
    private Map<String, Long> deliveryCount;

    /**
     * 投放清单
     */
    private List<DeliveryProjectDetail> detailList;

}
