package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.PackTime;

import javax.validation.constraints.NotNull;

@Data
public class DeliveryProfileRequest {

    private String applyAlias;

    @NotNull(message = "运行环境的guid不能为空")
    private Integer guidProfiles;

    @NotNull(message = "打包窗口不能为空")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private PackTime packTiming;
}
