package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.PackTime;

import javax.validation.constraints.NotNull;

@Data
public class ProfilesReq {

    @NotNull(message = "环境id不能为空！")
    private Integer guidProfiles;

    @NotNull(message = "打包窗口不能为空或窗口不存在！")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private PackTime packTiming;

}
