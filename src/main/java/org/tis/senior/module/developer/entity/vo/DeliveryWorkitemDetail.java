package org.tis.senior.module.developer.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.mapper.SqlCondition;
import lombok.Data;
import org.tis.senior.module.core.entity.cascade.CommonCascadeSerializer;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.DeliveryResult;
import org.tis.senior.module.developer.entity.enums.DeliveryType;

import java.util.Date;

@Data
public class DeliveryWorkitemDetail {

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 申请别名:申请人为本次申请唯一设定的名称，用来自我识别
     */
    private String applyAlias;

    /**
     * 工作项GUID:唯一标示某条数据（自增长）
     */
    @JSONField(serializeUsing = CommonCascadeSerializer.class)
    private Integer guidWorkitem;

    /**
     * 运行环境GUID:唯一标示某条数据（自增长）
     */
    @JSONField(serializeUsing = CommonCascadeSerializer.class)
    private Integer guidProfiles;

    /**
     * 投放类型：G 普通申请
     * M 合并申请
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private DeliveryType deliveryType;

    /**
     * 投放申请人:提出投放申请的开发人员
     */
    @TableField(condition = SqlCondition.LIKE)
    private String proposer;

    /**
     * 提出申请时间
     */
    private Date applyTime;

    /**
     * 投放说明:对投放结果的说明，如：因为合并代码与申请中投放代码数量不符，RCT投放失败，此处说明该原因
     */
    private String deliveryDesc;

    /**
     * 投放处理人:谁处理了这个投放申请，一般记录RCT小组成员
     */
    private String deliver;

    /**
     * 打包窗口:本环境对应的打包窗口时间，用业务字典来实现
     * 如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00
     */
    private String packTiming;

    /**
     * 投放时间
     */
    private Date deliveryTime;

    /**
     * 投放结果:0 申请中
     * S 成功
     * F 失败
     * C 取消投放（功能没有投放）
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private DeliveryResult deliveryResult;

    /**
     * 合并清单：合并投放申请的id集合，以，分割
     */
    private String mergeList;

    /**
     * 工作项名称
     */
    private String itemName;
}
