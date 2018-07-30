package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.core.entity.request.RestRequest;
import org.tis.senior.module.developer.entity.enums.BranchType;

import javax.validation.constraints.NotNull;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/30
 **/
@Data
public class WorkItemAddBranchRequest  extends RestRequest {

    @NotNull(message = "分支的类型不能为空！")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private BranchType branchType;

    @URL(message = "URL不合法！")
    @NotBlank(message = "分支路径不能为空！")
    private String fullPath;

    @NotBlank(message = "提交注释不能为空！")
    private String message;

    @NotBlank(message = "分支说明不能为空")
    private String branchFor;

}
