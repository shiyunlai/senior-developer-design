package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SProject;

import java.util.*;
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
     * 工程类型
     */
    private String projectType;

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

    public static List<DeliveryProjectDetail> getDeliveryDetail(List<SDeliveryList> deliveryLists,
                                                                List<SProject> projects) {
        Map<String, SProject> pjMap = projects.stream().collect(Collectors.toMap(SProject::getProjectName, p -> p));
        List<DeliveryProjectDetail> details = new ArrayList<>();
        deliveryLists.stream()
                .collect(Collectors.groupingBy(SDeliveryList::getPartOfProject))
                .forEach((p, l) -> {
                    DeliveryProjectDetail detail = new DeliveryProjectDetail();
                    if (pjMap.get(p) == null) {
                        detail.setProjectType("S");
                    } else {
                        detail.setProjectType(pjMap.get(p).getProjectType());
                    }
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
