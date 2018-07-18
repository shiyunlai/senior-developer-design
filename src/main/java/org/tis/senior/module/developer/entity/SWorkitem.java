package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.SqlCondition;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.ItemStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * sWorkitem某个需要开发的需求或项目
 * SALM管理流程：一个工作项对应一个唯一的开发分支（如果开发组内要多分枝，请自行git进行管理，但是对于交付来说工作项唯一对应一个开发分支）
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Data
@TableName("s_workitem")
public class SWorkitem implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "工作项";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * itemName对应表字段
     */
    public static final String COLUMN_ITEM_NAME = "item_name";

    /**
     * seqno对应表字段
     */
    public static final String COLUMN_SEQNO = "seqno";

    /**
     * developers对应表字段
     */
    public static final String COLUMN_DEVELOPERS = "developers";

    /**
     * owner对应表字段
     */
    public static final String COLUMN_OWNER = "owner";

    /**
     * requirementDesc对应表字段
     */
    public static final String COLUMN_REQUIREMENT_DESC = "requirement_desc";

    /**
     * receiveTime对应表字段
     */
    public static final String COLUMN_RECEIVE_TIME = "receive_time";

    /**
     * developStartTime对应表字段
     */
    public static final String COLUMN_DEVELOP_START_TIME = "develop_start_time";

    /**
     * deliveryPlanTime对应表字段
     */
    public static final String COLUMN_DELIVERY_PLAN_TIME = "delivery_plan_time";

    /**
     * deliveryTime对应表字段
     */
    public static final String COLUMN_DELIVERY_TIME = "delivery_time";

    /**
     * itemStatus对应表字段
     */
    public static final String COLUMN_ITEM_STATUS = "item_status";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * itemName逻辑名
     */
    public static final String NAME_ITEM_NAME = "工作项名称";

    /**
     * seqno逻辑名
     */
    public static final String NAME_SEQNO = "需求编号";

    /**
     * developers逻辑名
     */
    public static final String NAME_DEVELOPERS = "开发人员";

    /**
     * owner逻辑名
     */
    public static final String NAME_OWNER = "工作项负责人";

    /**
     * requirementDesc逻辑名
     */
    public static final String NAME_REQUIREMENT_DESC = "需求描述";

    /**
     * receiveTime逻辑名
     */
    public static final String NAME_RECEIVE_TIME = "收到需求时间";

    /**
     * developStartTime逻辑名
     */
    public static final String NAME_DEVELOP_START_TIME = "启动开发时间";

    /**
     * deliveryPlanTime逻辑名
     */
    public static final String NAME_DELIVERY_PLAN_TIME = "计划投产时间";

    /**
     * deliveryTime逻辑名
     */
    public static final String NAME_DELIVERY_TIME = "实际投产时间";

    /**
     * itemStatus逻辑名
     */
    public static final String NAME_ITEM_STATUS = "工作项状态";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 工作项名称
     */
    @TableField(condition = SqlCondition.LIKE)
    private String itemName;

    /**
     * 需求编号:开发内容对应的需求编号（有需求编号的才进入系统）
     */
    @TableField(condition = SqlCondition.LIKE)
    private String seqno;

    /**
     * 开发人员:一个工作项可有多个开发人员
     */
    private String developers;

    /**
     * 工作项负责人:本工作项的负责人（对需求、测试、最终投产负责）
     */
    private String owner;

    /**
     * 需求描述:工作项对应的需求简述
     */
    private String requirementDesc;

    /**
     * 收到需求时间
     */
    private Date receiveTime;

    /**
     * 启动开发时间
     */
    private Date developStartTime;

    /**
     * 计划投产时间
     */
    private Date deliveryPlanTime;

    /**
     * 实际投产时间
     */
    private Date deliveryTime;

    /**
     * 工作项状态:0 开发中
     * 1 已投产 （ 不能再提交投放申请）
     * 2 已取消 （新建后，不再使用）
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private ItemStatus itemStatus;

}

