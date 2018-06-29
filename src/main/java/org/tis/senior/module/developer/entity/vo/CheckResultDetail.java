package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SMergeList;

import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/29
 **/
@Data
public class CheckResultDetail {

    private List<DeliveryCheckResultDetail> deliveryDetails;

    private List<SMergeList> mergeLists;
}
