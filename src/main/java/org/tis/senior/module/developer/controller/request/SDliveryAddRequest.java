package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.PackTime;

import java.util.Date;
import java.util.List;

@Data
public class SDliveryAddRequest {


    private String ApplyAlias;

    @NotBlank(message = "工作项的guid不能为空")
    private Integer guidWorkitem;

    @NotEmpty(message = "投放的运行环境guid不能为空")
    private List<Integer> guidProfiles;

    @NotBlank(message = "投放说明不能为空")
    private String deliveryDesc;

    @NotBlank(message = "投放窗口不能为空")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private PackTime packTiming;

    @NotBlank(message = "投放时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryTime;
}
