package org.tis.senior.module.developer.controller.request;

import lombok.Data;
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

    @NotNull(message = "投放结果不能为空或格式错误！")
    private DeliveryResult result;

    private String desc;
}
