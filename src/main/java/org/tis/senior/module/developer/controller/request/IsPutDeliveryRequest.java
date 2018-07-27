package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IsPutDeliveryRequest {

    @NotNull(message = "工作项的guid不能为空！")
    private Integer guidWorkitem;

    @Valid
    @NotEmpty(message = "投放的运行环境不能为空")
    private List<DeliveryProfileRequest> profiles;
}
