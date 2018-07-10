package org.tis.senior.module.developer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProfileAndBranchAddRequest {

    @NotNull(message = "新增运行环境的对象不能为空")
    private ProfileAddAndUpdateRequest profileAddRequest;

    @NotNull
    private BranchAddAndUpdateRequest branchAddRequest;
}
