package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class DeliveryListAndDeliveryAddRequest {

    @NotEmpty
    private SDliveryAddRequest dliveryAddRequest;

    @NotEmpty
    private List<DeliveryListAddRequest> DdliveryList;

    private String guidBranch;
}
