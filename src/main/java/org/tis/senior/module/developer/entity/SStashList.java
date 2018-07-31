package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.CommitType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * sStashList
 * 
 * @author Auto Generate Tools
 * @date 2018/07/30
 */
@Data
@TableName("s_stash_list")
public class SStashList implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "贮藏清单";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidWorkitem对应表字段
     */
    public static final String COLUMN_GUID_WORKITEM = "guid_workitem";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * patchType对应表字段
     */
    public static final String COLUMN_PATCH_TYPE = "patch_type";

    /**
     * deployWhere对应表字段
     */
    public static final String COLUMN_DEPLOY_WHERE = "deploy_where";

    /**
     * fullPath对应表字段
     */
    public static final String COLUMN_FULL_PATH = "full_path";

    /**
     * partOfProject对应表字段
     */
    public static final String COLUMN_PART_OF_PROJECT = "part_of_project";

    /**
     * commitType对应表字段
     */
    public static final String COLUMN_COMMIT_TYPE = "commit_type";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidWorkitem逻辑名
     */
    public static final String NAME_GUID_WORKITEM = "工作项GUID";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * patchType逻辑名
     */
    public static final String NAME_PATCH_TYPE = "补丁类型";

    /**
     * deployWhere逻辑名
     */
    public static final String NAME_DEPLOY_WHERE = "部署到";

    /**
     * fullPath逻辑名
     */
    public static final String NAME_FULL_PATH = "代码全路径";

    /**
     * partOfProject逻辑名
     */
    public static final String NAME_PART_OF_PROJECT = "代码所在工程";

    /**
     * commitType逻辑名
     */
    public static final String NAME_COMMIT_TYPE = "提交类型";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 工作项GUID:唯一标示某条数据（自增长）
     */
    private Integer guidWorkitem;

    /**
     * 程序名称:冗余设计
     */
    @NotBlank(message = "程序名称不能为空！")
    private String programName;

    /**
     * 补丁类型:JAR 输出为jar包
     * ECD 输出为ecd包
     * EPD 输出为epd
     * CFG 作为配置文件
     * DBV 作为数据库脚本（SQL、DDL等数据库版本脚本）
     */
    @NotBlank(message = "导出类型不能为空！")
    private String patchType;

    /**
     * 部署到
     */
    @NotBlank(message = "部署到哪不能为空！")
    private String deployWhere;

    /**
     * 代码全路径
     */
    @NotBlank(message = "代码全路径不能为空！")
    private String fullPath;

    /**
     * 代码所在工程:记录该代码所在的工程名称（s_project.project_name）
     * 冗余设计
     */
    @NotBlank(message = "代码所在工程不能为空！")
    private String partOfProject;

    /**
     * 提交类型:提交类型
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    @NotNull(message = "提交类型不能为空！")
    private CommitType commitType;

}

