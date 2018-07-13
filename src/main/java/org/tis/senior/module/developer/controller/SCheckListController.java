package org.tis.senior.module.developer.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.controller.request.SCheckListDeliveryRequest;
import org.tis.senior.module.developer.entity.enums.ConfirmStatus;
import org.tis.senior.module.developer.service.ISCheckService;


/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/12
 **/
@RestController
@RequestMapping("/checkLists")
public class SCheckListController {

    @Autowired
    private ISCheckService checkService;

    /**
     * 确认投放清单明细
     * @param id
     * @return
     */
    @RequiresRoles(value = "rct")
    @PutMapping("/{id}/status/{status}")
    ResultVO confirm(@PathVariable @NotBlank(message = "核对清单ID不能为空") String id,
                             @PathVariable @NotBlank(message = "核对状态不能为空") String status) {
        ConfirmStatus statu = ConfirmStatus.what(status);
        if (statu.equals(ConfirmStatus.DELIVERY)) {
            return ResultVO.failure("400", "该接口不支持处理添加投放操作!");
        }
        checkService.confirm(id, statu);
        return ResultVO.success("操作成功！");
    }

    /**
     * 确认投放清单明细-添加到投放申请
     * @param id
     * @return
     */
    @RequiresRoles(value = "rct")
    @PutMapping("/{id}/delivery")
    ResultVO delivery(@PathVariable @NotBlank(message = "核对清单ID不能为空") String id,
                      @RequestBody @Validated SCheckListDeliveryRequest request) {
        checkService.confirmToDelivery(id, request.getGuidDelivery(), request.getPatchType(), request.getDeployWhere());
        return ResultVO.success("操作成功！");
    }


}
