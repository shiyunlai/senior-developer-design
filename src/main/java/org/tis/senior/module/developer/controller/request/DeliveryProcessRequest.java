package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.request.RestRequest;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;

import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/1
 **/
@Data
public class DeliveryProcessRequest extends RestRequest {

    @NotBlank(message = "投放申请id不能为空！")
    private String deliveryGuid;

    @NotNull(message = "投放结果不能为空或格式错误！")
    private DeliveryResult result;

    private String desc;
}
