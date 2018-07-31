package org.tis.senior.module.developer.entity.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.tis.senior.module.developer.entity.SCheckList;
import org.tis.senior.module.developer.entity.SDeliveryList;
import org.tis.senior.module.developer.entity.SProject;
import org.tis.senior.module.developer.entity.enums.ProjectType;
import org.tis.senior.module.developer.util.DeveloperUtils;

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
                        detail.setProjectType(ProjectType.SPECIAL.getValue().toString());
                    } else {
                        detail.setProjectType(pjMap.get(p).getProjectType().getValue().toString());
                    }
                    detail.setProjectName(p);
                    List<DeliveryPatchDetail> dpts = new ArrayList<>();
                    l.stream().collect(Collectors.groupingBy(SDeliveryList::getPatchType)).forEach((pt, list) -> {
                        DeliveryPatchDetail dpt = new DeliveryPatchDetail();
                        dpt.setPatchType(pt);
                        if (pjMap.get(p).getProjectType().equals(ProjectType.SPECIAL)){
                            dpt.setDeployWhere(list.get(0).getDeployWhere());
                        }else{
                            dpt.setDeployWhere(DeveloperUtils.getDeployWhere(pt,list.get(0).getDeployWhere()));
                        }
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
                        dpt.setDeployWhere(DeveloperUtils.getDeployWhere(pt,list.get(0).getDeployWhere()));
                        dpt.setFileList(list);
                        dpts.add(dpt);
                        detail.setDeliveryPatchDetails(dpts);
                    });
                    details.add(detail);
                });
        return details;
    }

    public static List<DeliveryProjectDetail> getCheckDeliveryDetail(List<SCheckList> deliveryLists) {
        List<DeliveryProjectDetail> details = new ArrayList<>();
        deliveryLists.stream()
                .collect(Collectors.groupingBy(SCheckList::getPartOfProject))
                .forEach((p, l) -> {
                    DeliveryProjectDetail detail = new DeliveryProjectDetail();
                    detail.setProjectName(p);
                    List<DeliveryPatchDetail> dpts = new ArrayList<>();
                    l.stream().collect(Collectors.groupingBy(SCheckList::getPatchType)).forEach((pt, list) -> {
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

    /**
     * 获取部署类型
     * @param patchType 导出类型
     * @param deployConfig 导出配置
     * @return
     */
    public static String generateDeployWhereString(String patchType, String deployConfig) {
        JSONArray configs = JSONArray.parseArray(deployConfig);
        JSONObject j = new JSONObject();
        String[] types = patchType.split(",");
        for (String type : types) {
            if (StringUtils.isNotBlank(type)) {
                for (Object config : configs) {
                    JSONObject jsonObject = JSONObject.parseObject(config.toString());
                    if (type.equals(jsonObject.getString("exportType"))) {
                        j.put(type, jsonObject.getString("deployType"));
                    }
                }

            }
        }
        return j.toJSONString();
    }

    public static void main(String[] args) {
        List<String> str = new ArrayList<>();
        str.add("111");
        str.add("222");
        str.add("333");
        str.add("444");
        for (String s:str){
            if(s.equals("111")){
                str.remove(s);
            }
        }
        System.out.println(str.size());
    }

}
