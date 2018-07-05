package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.tis.senior.module.core.entity.request.RestRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/21
 **/
@Data
public class MergeDeliveryRequest extends RestRequest {

    @NotEmpty(message = "合并清单不能为空！")
    private List<String> mergeList;

    @NotNull(message = "投产时间不能为空！")
    private Date deliveryTime;

    @Valid
    @NotEmpty(message = "环境信息不能为空！")
    private List<DeliveryProfileRequest> profiles;

}

