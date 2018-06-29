package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class DeliveryListAndDeliveryAddRequest {

    @NotEmpty(message = "投产申请信息不能为空")
    private SDliveryAddRequest dliveryAddRequest;

    @NotEmpty(message = "投产清单代码不能为空")
    private List<DeliveryListAddRequest> DdliveryList;

    @NotBlank(message = "分支的guid不能为空")
    private String guidBranch;
}
