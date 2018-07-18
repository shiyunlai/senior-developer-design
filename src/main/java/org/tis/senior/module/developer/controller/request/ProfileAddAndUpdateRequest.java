package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ProfileAddAndUpdateRequest {

    public interface add{}
    public interface update{}

    @Null(message = "分支guid必须为空！", groups = {ProfileAddAndUpdateRequest.add.class})
    @NotNull(message = "分支guid不能为空！", groups = {ProfileAddAndUpdateRequest.update.class})
    private Integer guid;

    @NotBlank(message = "运行环境的名称不能空")
    private String profilesName;

    @NotBlank(message = "运行环境的主机IP不能空")
    private String hostIp;

    @NotBlank(message = "运行环境的安装路径不能空")
    private String installPath;

    @NotBlank(message = "运行环境的登录用户名不能空")
    private String csvUser;

    @NotBlank(message = "运行环境的登录密码不能空")
    private String csvPwd;

    @NotBlank(message = "运行环境的管理员不能空")
    private String manager;

    @NotBlank(message = "运行环境的打包窗口不能空")
    private String packTiming;
}
