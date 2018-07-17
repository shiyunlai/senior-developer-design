package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.PackTime;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DeliveryOutExeclRequest {


    @NotNull(message = "投放时间不能为空")
    private Date deliveryTime;

    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    @NotNull(message = "打包窗口不能为空")
    private PackTime packTiming;

    @NotNull(message = "运行环境guid不能为空")
    private Integer guidProfile;
}
