package org.tis.senior.module.developer.entity.vo;

import lombok.Data;
import org.tis.senior.module.developer.entity.SProject;

import java.util.List;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/7/30
 **/
@Data
public class ProjectDetail {

    private List<SProject> own;

    private List<SProject> others;

}
