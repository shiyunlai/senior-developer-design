package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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

    @NotBlank(message = "工程类型不能为空！")
    private String projectType;

    @NotBlank(message = "工程的导出类型和部署类型不能为空！")
    private String deployConfig;
}
