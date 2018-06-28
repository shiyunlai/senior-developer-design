package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.enums.PatchType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class DeliveryProjectDetail {

    /**
     * 工程名
     */
    private String projectName;

    /**
     * 编译为
     */
    private List<String> patchName;

    /**
     * 部署到 TODO 代码级还是工程级
     */
    private List<String> profileName;

    /**
     * 投放清单
     */
    private List<SDeliveryList> fileList;


    public static List<DeliveryProjectDetail> getDeliveryDetail(List<SDeliveryList> deliveryLists) {
        List<DeliveryProjectDetail> details = new ArrayList<>();
        deliveryLists.stream()
                .collect(Collectors.groupingBy(SDeliveryList::getPartOfProject))
                .forEach((p, l) -> {
                    DeliveryProjectDetail detail = new DeliveryProjectDetail();
                    detail.setProjectName(p);
                    List<String> types = l.stream().map(SDeliveryList::getPatchType).distinct().collect(Collectors.toList());
                    detail.setPatchName(types);
                    detail.setFileList(l);
                    details.add(detail);
                });
        return details;
    }


}
