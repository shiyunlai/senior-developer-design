package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDeliveryList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeliveryListAndDeliveryAddRequest {

    @NotNull(message = "工作项的guid不能为空！")
    private Integer guidWorkitem;

    @Valid
    @NotNull(message = "投产申请信息不能为空")
    private SDliveryAddRequest dliveryAddRequest;

    private List<SDeliveryList> deliveryList;

    @NotNull(message = "分支的guid不能为空")
    private Integer guidBranch;
}
