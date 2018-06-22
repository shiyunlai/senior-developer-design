package org.tis.senior.module.developer.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class DeliveryProjectDetail {

    private String projectName;

    private String compileName;

    private List<SvnFile> fileList;

}
