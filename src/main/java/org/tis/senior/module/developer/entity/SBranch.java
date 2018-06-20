package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sBranch开发分支
 * 1、某个工作项对应唯一的开发分支
 * 2、某个环境根据需要可以更换分支（如：SIT曾经使用过3个分支）
 * 3、每个分支上哪些代码做过修改
 * 4、开发人员对每个代码的提交历史
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_branch")
public class SBranch implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "分支";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * branchType对应表字段
     */
    public static final String COLUMN_BRANCH_TYPE = "branch_type";

    /**
     * fullPath对应表字段
     */
    public static final String COLUMN_FULL_PATH = "full_path";

    /**
     * creater对应表字段
     */
    public static final String COLUMN_CREATER = "creater";

    /**
     * createTime对应表字段
     */
    public static final String COLUMN_CREATE_TIME = "create_time";

    /**
     * branchFor对应表字段
     */
    public static final String COLUMN_BRANCH_FOR = "branch_for";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * branchType逻辑名
     */
    public static final String NAME_BRANCH_TYPE = "分支类型";

    /**
     * fullPath逻辑名
     */
    public static final String NAME_FULL_PATH = "代码全路径";

    /**
     * creater逻辑名
     */
    public static final String NAME_CREATER = "创建人";

    /**
     * createTime逻辑名
     */
    public static final String NAME_CREATE_TIME = "创建时间";

    /**
     * branchFor逻辑名
     */
    public static final String NAME_BRANCH_FOR = "分支作用说明";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 分支类型:F 特性分支，普通功能/项目对一个的分支
     * H hot分支，修复生产bug，或开发紧急投产内容的分支
     * R release分支
     */
    private String branchType;

    /**
     * 代码全路径:冗余设计
     */
    private String fullPath;

    /**
     * 创建人:分支创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 分支作用说明:创建这个分支的目的说明
     */
    private String branchFor;

}

