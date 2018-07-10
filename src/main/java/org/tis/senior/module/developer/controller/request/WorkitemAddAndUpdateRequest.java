package org.tis.senior.module.developer.controller.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
public class WorkitemAddAndUpdateRequest {

    public interface add{}
    public interface update{}

    @Null(message = "工作项guid必须为空！", groups = {WorkitemAddAndUpdateRequest.add.class})
    @NotNull(message = "工作项guid不能为空！", groups = {WorkitemAddAndUpdateRequest.update.class})
    private Integer guid;

    @NotBlank(message = "工作项名称不能为空")
    private String itemName;

    @NotBlank(message = "工作项编号不能为空")
    private String seqno;

    @NotBlank(message = "工作项开发人员不能为空")
    private String developers;

    @NotBlank(message = "工作项负责人不能不空")
    private String owner;

    @NotBlank(message = "工作项需求描述不能为空")
    private String requirementDesc;

    private Date receiveTime;

    private Date developStartTime;

    private Date deliveryPlanTime;

    private Date deliveryTime;
}
