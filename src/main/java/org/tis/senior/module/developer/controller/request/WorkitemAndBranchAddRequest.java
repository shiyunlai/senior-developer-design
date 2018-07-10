package org.tis.senior.module.developer.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WorkitemAndBranchAddRequest {

    @NotNull(message = "新增的工作项对象不能为空")
    private WorkitemAddAndUpdateRequest workitemUpdateRequest;

    @NotNull(message = "新增的分支对象不能为空")
    private BranchAddAndUpdateRequest branchAddRequest;
}
