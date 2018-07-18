package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.SqlCondition;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.ProjectType;

import java.io.Serializable;

/**
 * sProject记录了TIP中所有的工程，以及布丁形式，和可部署的位置信息
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
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
     * projectName对应表字段
     */
    public static final String COLUMN_PROJECT_NAME = "project_name";

    /**
     * projectType对应表字段
     */
    public static final String COLUMN_PROJECT_TYPE = "project_type";

    /**
     * deployConfig对应表字段
     */
    public static final String COLUMN_DEPLOY_CONFIG = "deploy_config";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * projectName逻辑名
     */
    public static final String NAME_PROJECT_NAME = "工程名称";

    /**
     * projectType逻辑名
     */
    public static final String NAME_PROJECT_TYPE = "工程类型";

    /**
     * deployConfig逻辑名
     */
    public static final String NAME_DEPLOY_CONFIG = "部署配置";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 工程名称
     */
    @TableField(condition = SqlCondition.LIKE)
    private String projectName;

    /**
     * 工程类型
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private ProjectType projectType;

    /**
     * 部署配置:该工程可以部署到哪些子系统
     * 用json的方式存储，前端解析后，提供多选
     */
    private String deployConfig;

}

