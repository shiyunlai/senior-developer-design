package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.ProjectType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ProjectAddAndUpdateRequest {

    public interface add{}
    public interface update{}

    @Null(message = "工程guid必须为空！", groups = {ProjectAddAndUpdateRequest.add.class})
    @NotNull(message = "工程guid不能为空！", groups = {ProjectAddAndUpdateRequest.update.class})
    private Integer guid;

    @NotBlank(message = "工程名不能为空！")
    private String projectName;

    @NotNull(message = "工程类型不能为空！")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private ProjectType projectType;

    @NotBlank(message = "工程的导出类型和部署类型不能为空！")
    private String deployConfig;
}
