package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.enums.SvnPathType;

import java.util.Date;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
@Data
public class SvnFile {

    private String revision;

    private String author;

    private Date data;

    private SvnPathType type;

    private String path;
}
