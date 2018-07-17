package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDelivery;

import java.util.List;

/**
 * description:
 *
 * @author lijh
 * @date 2018/7/16
 **/
@Data
public class DeilveryResult {

    private List<DeliveryProjectDetail> deliveryProjectDetails;

    private List<SDelivery> deliveries;
}
