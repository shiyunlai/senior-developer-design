package org.tis.senior.module.developer.controller.request;

import lombok.Data;
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

    @NotEmpty(message = "工程id不能为空！")
    private List<String> projectGuids;

}
