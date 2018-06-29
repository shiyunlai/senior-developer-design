package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDeliveryList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private List<DeliveryPatchDetail> deliveryPatchDetails;


    public Set<String> getPatchType() {
        Set<String> count = new HashSet<>();
        this.deliveryPatchDetails.forEach(dpd -> {
            String[] split = dpd.getPatchType().split(",");
            for (String p : split) {
                count.add(p);
            }
        });
        return count;

    }

    public static List<DeliveryProjectDetail> getDeliveryDetail(List<SDeliveryList> deliveryLists) {
        List<DeliveryProjectDetail> details = new ArrayList<>();
        deliveryLists.stream()
                .collect(Collectors.groupingBy(SDeliveryList::getPartOfProject))
                .forEach((p, l) -> {
                    DeliveryProjectDetail detail = new DeliveryProjectDetail();
                    detail.setProjectName(p);
                    List<DeliveryPatchDetail> dpts = new ArrayList<>();
                    l.stream().collect(Collectors.groupingBy(SDeliveryList::getPatchType)).forEach((pt, list) -> {
                        DeliveryPatchDetail dpt = new DeliveryPatchDetail();
                        dpt.setPatchType(pt);
                        dpt.setDeployWhere(list.get(0).getDeployWhere());
                        dpt.setFileList(list);
                        dpts.add(dpt);
                        detail.setDeliveryPatchDetails(dpts);
                    });
                    details.add(detail);
                });
        return details;
    }


}
