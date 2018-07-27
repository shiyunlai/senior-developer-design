package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SDeliveryUpdateRequest {

    @NotNull(message = "投放申请的guid不能为空！")
    private Integer guidDelivery;

    @NotNull(message = "投放的时间不能为空！")
    private Date deliveryTime;

    @NotBlank(message = "投放的打包窗口不能为空！")
    private String packTiming;
}
