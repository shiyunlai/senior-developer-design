package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SDliveryAddRequest {


    @NotBlank(message = "申请别名不能为空")
    private String applyAlias;

    @Valid
    @NotEmpty(message = "投放的运行环境不能为空")
    private List<DeliveryProfileRequest> profiles;

    @NotNull(message = "投放时间不能为空")
    private Date deliveryTime;
}
