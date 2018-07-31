package org.tis.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PackTimeVerify {

    private List<PackTimeDetail> packTimeDetails;

    private Date deliveryTime;

}
