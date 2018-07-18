package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class VerifcationUrlRequest {

    @NotBlank(message = "需要验证的svnUrl不能为空！")
    private String svnUrl;
}
