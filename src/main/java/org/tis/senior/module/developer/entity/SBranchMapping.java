package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.BranchForWhat;
import org.tis.senior.module.developer.entity.enums.BranchMappingStatus;

import java.io.Serializable;

/**
 * sBranchMapping记录了分支与开发目的之间的对应关系
 * 每次指定记录为一条记录
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Data
@TableName("s_branch_mapping")
public class SBranchMapping implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "分支用途对照";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidBranch对应表字段
     */
    public static final String COLUMN_GUID_BRANCH = "guid_branch";

    /**
     * forWhat对应表字段
     */
    public static final String COLUMN_FOR_WHAT = "for_what";

    /**
     * guidOfWhats对应表字段
     */
    public static final String COLUMN_GUID_OF_WHATS = "guid_of_whats";

    /**
     * allotTime对应表字段
     */
    public static final String COLUMN_ALLOT_TIME = "allot_time";

    /**
     * status对应表字段
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidBranch逻辑名
     */
    public static final String NAME_GUID_BRANCH = "分支的GUID";

    /**
     * forWhat逻辑名
     */
    public static final String NAME_FOR_WHAT = "为何而创建分支";

    /**
     * guidOfWhats逻辑名
     */
    public static final String NAME_GUID_OF_WHATS = "何的guid";

    /**
     * allotTime逻辑名
     */
    public static final String NAME_ALLOT_TIME = "指配时间";

    /**
     * status逻辑名
     */
    public static final String NAME_STATUS = "状态";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 分支的GUID:唯一标示某条数据（自增长）
     */
    private Integer guidBranch;

    /**
     * 为何而创建分支:W  为开发项（Workitem）
     * R   为运行环境发版（Release）
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private BranchForWhat forWhat;

    /**
     * 何的guid:工作项/运行环境的GUID
     */
    private Integer guidOfWhats;

    /**
     * 指配时间
     */
    private Date allotTime;

    /**
     * 状态:1 生效
     * 0 不再生效
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private BranchMappingStatus status;

}

