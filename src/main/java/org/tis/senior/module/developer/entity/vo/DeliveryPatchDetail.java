package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDeliveryList;

import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/28
 **/
@Data
public class DeliveryPatchDetail {

    private String patchType;

    /**
     * 部署到
     */
    private String deployWhere;

    /**
     * 投放清单
     */
    private List<SDeliveryList> fileList;
}
