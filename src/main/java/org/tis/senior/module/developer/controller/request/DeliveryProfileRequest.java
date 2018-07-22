package org.tis.senior.module.developer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DeliveryProfileRequest {

    private String applyAlias;

    @NotNull(message = "运行环境的guid不能为空")
    private Integer guidProfiles;

    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d$", message = "打包窗口不能为空或格式错误")
    private String packTiming;
}
