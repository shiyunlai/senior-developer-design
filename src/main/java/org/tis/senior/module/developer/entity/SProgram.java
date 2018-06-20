package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sProgram从分支创建后开始，收集并记录该分支中被提交过的程序，如：java、config、脚本等代码文件
 * （不再手工收集补丁清单）
 * 同一个程序会被多次申请，投产到对应的环境
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_program")
public class SProgram implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "程序文件";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidBranch对应表字段
     */
    public static final String COLUMN_GUID_BRANCH = "guid_branch";

    /**
     * guidProject对应表字段
     */
    public static final String COLUMN_GUID_PROJECT = "guid_project";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * devReason对应表字段
     */
    public static final String COLUMN_DEV_REASON = "dev_reason";

    /**
     * programPath对应表字段
     */
    public static final String COLUMN_PROGRAM_PATH = "program_path";

    /**
     * partOfProject对应表字段
     */
    public static final String COLUMN_PART_OF_PROJECT = "part_of_project";

    /**
     * startVersion对应表字段
     */
    public static final String COLUMN_START_VERSION = "start_version";

    /**
     * currVersion对应表字段
     */
    public static final String COLUMN_CURR_VERSION = "curr_version";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidBranch逻辑名
     */
    public static final String NAME_GUID_BRANCH = "分支GUID";

    /**
     * guidProject逻辑名
     */
    public static final String NAME_GUID_PROJECT = "工程GUID";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * devReason逻辑名
     */
    public static final String NAME_DEV_REASON = "开发缘由";

    /**
     * programPath逻辑名
     */
    public static final String NAME_PROGRAM_PATH = "程序路径";

    /**
     * partOfProject逻辑名
     */
    public static final String NAME_PART_OF_PROJECT = "代码所在工程";

    /**
     * startVersion逻辑名
     */
    public static final String NAME_START_VERSION = "起始版本";

    /**
     * currVersion逻辑名
     */
    public static final String NAME_CURR_VERSION = "当前最新版本";

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
     * 工程GUID:唯一标示某条数据（自增长）
     */
    private Integer guidProject;

    /**
     * 程序名称:记录程序名称
     */
    private String programName;

    /**
     * 开发缘由:此程序文件为何被修改？
     * ADD 新增程序
     * DEL  删除程序
     * UP    修改程序
     */
    private String devReason;

    /**
     * 程序路径:程序位于分支上的相对路径
     */
    private String programPath;

    /**
     * 代码所在工程:记录该代码所在的工程名称（s_project.project_name）
     * 冗余设计
     */
    private String partOfProject;

    /**
     * 起始版本:分支创建好时的版本
     */
    private Integer startVersion;

    /**
     * 当前最新版本:经过开发后的当前版本
     */
    private Integer currVersion;

}

