package org.tis.senior.module.developer.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.mapper.SqlCondition;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.ItemStatus;

import java.util.Date;

/**
 * description:
 *
 * @author lijh
 * @date 2018/7/17
 **/
@Data
public class WorkitemBranchDetail {

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
     * 提交标识
     */
    private String artf;

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

    /**
     * 分支url信息
     */
    private String fullPath;
}
