package org.tis.senior.module.developer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class DeliveryOutExeclRequest {


    @NotNull(message = "投放时间不能为空")
    private Date deliveryTime;

    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d$", message = "打包窗口不能为空或格式错误")
    private String packTiming;

    @NotNull(message = "运行环境guid不能为空")
    private Integer guidProfile;
}
