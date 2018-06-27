package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sMergeList（RCT人员合并开发分支）其中合并了哪些程序文件
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@Data
@TableName("s_merge_list")
public class SMergeList implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "合并代码清单";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * checkGuid对应表字段
     */
    public static final String COLUMN_CHECK_GUID = "check_guid";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * mergeVersion对应表字段
     */
    public static final String COLUMN_MERGE_VERSION = "merge_version";

    /**
     * fullPath对应表字段
     */
    public static final String COLUMN_FULL_PATH = "full_path";

    /**
     * author对应表字段
     */
    public static final String COLUMN_AUTHOR = "author";

    /**
     * mergeDate对应表字段
     */
    public static final String COLUMN_MERGE_DATE = "merge_date";

    /**
     * mergeType对应表字段
     */
    public static final String COLUMN_MERGE_TYPE = "merge_type";

    /**
     * developerConfirm对应表字段
     */
    public static final String COLUMN_DEVELOPER_CONFIRM = "developer_confirm";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * checkGuid逻辑名
     */
    public static final String NAME_CHECK_GUID = "核对GUID";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * mergeVersion逻辑名
     */
    public static final String NAME_MERGE_VERSION = "提交后新版本号";

    /**
     * fullPath逻辑名
     */
    public static final String NAME_FULL_PATH = "代码全路径";

    /**
     * author逻辑名
     */
    public static final String NAME_AUTHOR = "合并人员";

    /**
     * mergeDate逻辑名
     */
    public static final String NAME_MERGE_DATE = "合并时间";

    /**
     * mergeType逻辑名
     */
    public static final String NAME_MERGE_TYPE = "合并操作类型";

    /**
     * developerConfirm逻辑名
     */
    public static final String NAME_DEVELOPER_CONFIRM = "开发确认";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 核对GUID:唯一标示某条数据（自增长）
     */
    private Integer checkGuid;

    /**
     * 程序名称:冗余设计
     */
    private String programName;

    /**
     * 提交后新版本号:合并成功后，代码的新版本号
     */
    private Integer mergeVersion;

    /**
     * 代码全路径
     */
    private String fullPath;

    /**
     * 合并人员
     */
    private String author;

    /**
     * 合并时间
     */
    private Date mergeDate;

    /**
     * 合并操作类型:A  新增 Add
     * U  修改 Update
     * D  删除 Delete
     */
    private String mergeType;

    /**
     * 开发确认:合并后的代码都需要开发人员进行确认
     * 0 待确认
     * 1 确认
     * 2 有异议（代码合并有问题，需要线下手工处理）
     */
    private String developerConfirm;

}

