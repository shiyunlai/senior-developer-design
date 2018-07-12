package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;

import javax.validation.constraints.NotNull;

@Data
public class ProfileUpdateStatusRequest {

    @NotNull
    private Integer guid;

    @NotNull
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private IsAllowDelivery isAllowDelivery;
}
