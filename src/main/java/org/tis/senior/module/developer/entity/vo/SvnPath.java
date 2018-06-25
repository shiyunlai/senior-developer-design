package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.enums.SvnPathType;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class SvnPath {

    private SvnPathType type;

    private String path;

    private String copyPath;

    private Long copyRevision;
}
