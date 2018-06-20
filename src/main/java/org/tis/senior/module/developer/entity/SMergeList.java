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
 * @date 2018/06/19
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
     * guidDelivery对应表字段
     */
    public static final String COLUMN_GUID_DELIVERY = "guid_delivery";

    /**
     * guidFromBranch对应表字段
     */
    public static final String COLUMN_GUID_FROM_BRANCH = "guid_from_branch";

    /**
     * fromBranchPath对应表字段
     */
    public static final String COLUMN_FROM_BRANCH_PATH = "from_branch_path";

    /**
     * guidToBranch对应表字段
     */
    public static final String COLUMN_GUID_TO_BRANCH = "guid_to_branch";

    /**
     * toBranchPath对应表字段
     */
    public static final String COLUMN_TO_BRANCH_PATH = "to_branch_path";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * developer对应表字段
     */
    public static final String COLUMN_DEVELOPER = "developer";

    /**
     * mergeTime对应表字段
     */
    public static final String COLUMN_MERGE_TIME = "merge_time";

    /**
     * mergeOperator对应表字段
     */
    public static final String COLUMN_MERGE_OPERATOR = "merge_operator";

    /**
     * oldVersion对应表字段
     */
    public static final String COLUMN_OLD_VERSION = "old_version";

    /**
     * newVersion对应表字段
     */
    public static final String COLUMN_NEW_VERSION = "new_version";

    /**
     * developerConfirm对应表字段
     */
    public static final String COLUMN_DEVELOPER_CONFIRM = "developer_confirm";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidDelivery逻辑名
     */
    public static final String NAME_GUID_DELIVERY = "投放申请GUID";

    /**
     * guidFromBranch逻辑名
     */
    public static final String NAME_GUID_FROM_BRANCH = "被合并分支";

    /**
     * fromBranchPath逻辑名
     */
    public static final String NAME_FROM_BRANCH_PATH = "被合并分支路径";

    /**
     * guidToBranch逻辑名
     */
    public static final String NAME_GUID_TO_BRANCH = "接收合并分支";

    /**
     * toBranchPath逻辑名
     */
    public static final String NAME_TO_BRANCH_PATH = "接受合并分支路径";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * developer逻辑名
     */
    public static final String NAME_DEVELOPER = "开发人员";

    /**
     * mergeTime逻辑名
     */
    public static final String NAME_MERGE_TIME = "合并时间";

    /**
     * mergeOperator逻辑名
     */
    public static final String NAME_MERGE_OPERATOR = "合并操作类型";

    /**
     * oldVersion逻辑名
     */
    public static final String NAME_OLD_VERSION = "提交前代码版本";

    /**
     * newVersion逻辑名
     */
    public static final String NAME_NEW_VERSION = "提交后新版本号";

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
     * 投放申请GUID:唯一标示某条数据（自增长）
     */
    private Integer guidDelivery;

    /**
     * 被合并分支:来源分支
     */
    private Integer guidFromBranch;

    /**
     * 被合并分支路径:冗余字段，减少查询关联
     */
    private String fromBranchPath;

    /**
     * 接收合并分支:代码合并入分支
     */
    private Integer guidToBranch;

    /**
     * 接受合并分支路径:冗余字段，减少查询关联
     */
    private String toBranchPath;

    /**
     * 程序名称:冗余设计
     */
    private String programName;

    /**
     * 开发人员:记录开发人员的svn账号
     */
    private String developer;

    /**
     * 合并时间
     */
    private Date mergeTime;

    /**
     * 合并操作类型:A  新增 Add
     * U  修改 Update
     * D  删除 Delete
     */
    private String mergeOperator;

    /**
     * 提交前代码版本:记录代码在合并前的历史版本号（svn版本号）
     */
    private Integer oldVersion;

    /**
     * 提交后新版本号:合并成功后，代码的新版本号
     */
    private Integer newVersion;

    /**
     * 开发确认:合并后的代码都需要开发人员进行确认
     * 0 待确认
     * 1 确认
     * 2 有异议（代码合并有问题，需要线下手工处理）
     */
    private String developerConfirm;

}

