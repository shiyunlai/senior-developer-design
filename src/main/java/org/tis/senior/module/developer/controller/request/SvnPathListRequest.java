package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.CommitType;

import java.util.Date;

@Data
public class SvnPathListRequest {

    @NotBlank(message = "代码路径不能为空")
    private String path;

    @NotBlank(message = "程序名称不能为空")
    private String grogramName;

    @NotBlank(message = "版本号不能为空")
    private int revision;

    @NotBlank(message = "修改人不能为空")
    private String author;

    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private CommitType commitType;

    @NotBlank(message = "提交时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
}
