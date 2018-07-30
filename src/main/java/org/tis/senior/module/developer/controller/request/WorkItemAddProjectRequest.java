package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.tis.senior.module.core.entity.request.RestRequest;

import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/30
 **/
@Data
public class WorkItemAddProjectRequest extends RestRequest {

    @NotBlank(message = "提交注释不能为空！")
    private String message;

    @NotEmpty(message = "工程id不能为空！")
    private List<String> projectGuids;

}
