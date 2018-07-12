package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.tis.senior.module.developer.entity.SDeliveryList;

import javax.validation.Valid;
import java.util.List;

@Data
public class DeliveryListSuperadditionRequest {

    @NotEmpty(message = "追加的投放申请guid不能为空")
    private List<Integer> guidDelivery;

    @Valid
    @NotEmpty(message = "投产清单代码不能为空")
    private List<SDeliveryList> deliveryList;
}
