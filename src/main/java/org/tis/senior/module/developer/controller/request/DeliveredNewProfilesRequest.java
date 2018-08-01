package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description:
 *
 * @author ljh
 * @date 2018/7/31
 **/
@Data
public class DeliveredNewProfilesRequest {

    /**
     * 投到新环境的guid集合
     */
    @Valid
    @NotEmpty(message = "需要投放新环境的信息不能为空！")
    private List<DeliveryProfileRequest> profiles;

    /**
     * 被克隆投放的投放申请guid
     */
    @NotNull(message = "追加投放申请的guid不能为空")
    private Integer guidDelivery;
}
