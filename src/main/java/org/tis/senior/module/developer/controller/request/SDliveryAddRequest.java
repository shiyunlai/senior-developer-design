package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class SDliveryAddRequest {


    @NotEmpty(message = "投放的运行环境不能为空")
    private List<DeliveryProfileRequest> profiles;

}
