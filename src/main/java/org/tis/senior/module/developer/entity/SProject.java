package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sProject该分支中包括了哪些工程
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_project")
public class SProject implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "工程";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidBranch对应表字段
     */
    public static final String COLUMN_GUID_BRANCH = "guid_branch";

    /**
     * projectName对应表字段
     */
    public static final String COLUMN_PROJECT_NAME = "project_name";

    /**
     * deployType对应表字段
     */
    public static final String COLUMN_DEPLOY_TYPE = "deploy_type";

    /**
     * deployWhere对应表字段
     */
    public static final String COLUMN_DEPLOY_WHERE = "deploy_where";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidBranch逻辑名
     */
    public static final String NAME_GUID_BRANCH = "分支GUID";

    /**
     * projectName逻辑名
     */
    public static final String NAME_PROJECT_NAME = "工程名称";

    /**
     * deployType逻辑名
     */
    public static final String NAME_DEPLOY_TYPE = "部署类型";

    /**
     * deployWhere逻辑名
     */
    public static final String NAME_DEPLOY_WHERE = "部署到哪里";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 分支GUID:唯一标示某条数据（自增长）
     */
    private Integer guidBranch;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 部署类型:该工程以什么样的形式部署到系统中
     */
    private String deployType;

    /**
     * 部署到哪里:该工程部署到什么位置
     */
    private String deployWhere;

}

