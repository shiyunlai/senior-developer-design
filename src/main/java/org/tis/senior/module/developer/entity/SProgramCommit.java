package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sProgramCommit记录开发人员在分支上的代码提交历史
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_program_commit")
public class SProgramCommit implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "代码提交历史";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidProgram对应表字段
     */
    public static final String COLUMN_GUID_PROGRAM = "guid_program";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * commiter对应表字段
     */
    public static final String COLUMN_COMMITER = "commiter";

    /**
     * commitTime对应表字段
     */
    public static final String COLUMN_COMMIT_TIME = "commit_time";

    /**
     * commtType对应表字段
     */
    public static final String COLUMN_COMMT_TYPE = "commt_type";

    /**
     * oldVersion对应表字段
     */
    public static final String COLUMN_OLD_VERSION = "old_version";

    /**
     * newVersion对应表字段
     */
    public static final String COLUMN_NEW_VERSION = "new_version";

    /**
     * reviewer对应表字段
     */
    public static final String COLUMN_REVIEWER = "reviewer";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidProgram逻辑名
     */
    public static final String NAME_GUID_PROGRAM = "程序GUID";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * commiter逻辑名
     */
    public static final String NAME_COMMITER = "提交人";

    /**
     * commitTime逻辑名
     */
    public static final String NAME_COMMIT_TIME = "提交时间";

    /**
     * commtType逻辑名
     */
    public static final String NAME_COMMT_TYPE = "提交操作类型";

    /**
     * oldVersion逻辑名
     */
    public static final String NAME_OLD_VERSION = "提交前代码版本";

    /**
     * newVersion逻辑名
     */
    public static final String NAME_NEW_VERSION = "提交后新版本号";

    /**
     * reviewer逻辑名
     */
    public static final String NAME_REVIEWER = "代码走查人";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 程序GUID:唯一标示某条数据（自增长）
     */
    private Integer guidProgram;

    /**
     * 程序名称:冗余设计
     */
    private String programName;

    /**
     * 提交人
     */
    private String commiter;

    /**
     * 提交时间
     */
    private Date commitTime;

    /**
     * 提交操作类型:A  新增 Add
     * U  修改 Update
     * D  删除 Delete
     */
    private String commtType;

    /**
     * 提交前代码版本:记录代码在合并前的历史版本号（svn版本号）
     */
    private Integer oldVersion;

    /**
     * 提交后新版本号:合并成功后，代码的新版本号
     */
    private Integer newVersion;

    /**
     * 代码走查人
     */
    private String reviewer;

}

