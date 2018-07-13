package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/13
 **/
@Data
public class SCheckListDeliveryRequest {

    @NotBlank(message = "投放申请GUID不能为空！")
    private String guidDelivery;

    @NotBlank(message = "导出类型不能为空！")
    private String patchType;

    @NotBlank(message = "部署于不能为空！")
    private String deployWhere;
}
