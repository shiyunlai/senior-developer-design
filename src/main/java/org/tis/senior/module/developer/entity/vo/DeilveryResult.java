package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDelivery;

import java.util.List;

@Data
public class DeilveryResult {

    private List<DeliveryProjectDetail> deliveryProjectDetails;

    private List<SDelivery> deliveries;
}
