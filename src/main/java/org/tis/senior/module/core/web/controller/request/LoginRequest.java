package org.tis.senior.module.core.web.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.request.RestRequest;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/21
 **/
@Data
public class LoginRequest extends RestRequest {

    @NotBlank(message = "用户名不能为空")
    private String userId;

    @NotBlank(message = "密码不能为空")
    private String password;

}
