package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.tis.senior.module.developer.entity.SDeliveryList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeliveryListSuperadditionRequest {

    @NotNull(message = "工作项的guid不能为空！")
    private Integer guidWorkitem;

    @NotEmpty(message = "追加的投放申请guid不能为空！")
    private List<Integer> guidDelivery;

    @Valid
    @NotEmpty(message = "投产清单代码不能为空！")
    private List<SDeliveryList> deliveryList;
}
