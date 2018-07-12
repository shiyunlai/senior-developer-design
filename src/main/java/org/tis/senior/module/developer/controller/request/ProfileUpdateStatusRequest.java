package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;

import javax.validation.constraints.NotNull;

@Data
public class ProfileUpdateStatusRequest {

    @NotNull(message = "运行环境的guid不能为空")
    private Integer guid;

    @NotNull(message = "运行环境的状态不能为空")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private IsAllowDelivery isAllowDelivery;
}
