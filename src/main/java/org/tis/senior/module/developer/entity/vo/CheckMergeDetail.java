package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SDelivery;

import java.util.List;

/**
 * description: 合并详情
 *
 * @author zhaoch
 * @date 2018/7/11
 **/
@Data
public class CheckMergeDetail {

    private SDelivery delivery;

    private String branch;

    private List<String> projectList;

}
