package org.tis.senior.module.developer.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.BranchType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
public class BranchAddAndUpdateRequest {

    public interface add{}
    public interface update{}

    @Null(message = "分支guid必须为空！", groups = {BranchAddAndUpdateRequest.add.class})
    @NotNull(message = "分支guid不能为空！", groups = {BranchAddAndUpdateRequest.update.class})
    private Integer guid;

    @NotNull(message = "分支的类型不能为空")
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private BranchType branchType;

    @NotBlank(message = "分支的url不能为空")
    private String fullPath;

    @NotBlank(message = "分支说明不能为空")
    private String branchFor;

    @NotNull(message = "分支的起始版本号不能为空")
    private Integer lastVersion;

}
