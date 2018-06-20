package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sDelivery记录某个工作项的投放记录
 * 每次投放唯一对应一个投放申请（不对申请重复处理，但是可以提供 复制投放申请功能）
 * 工作项开发负责人提出申请，RCT人员处理申请
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_delivery")
public class SDelivery implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "投放申请";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * applyAlias对应表字段
     */
    public static final String COLUMN_APPLY_ALIAS = "apply_alias";

    /**
     * guidWorkitem对应表字段
     */
    public static final String COLUMN_GUID_WORKITEM = "guid_workitem";

    /**
     * guidProfiles对应表字段
     */
    public static final String COLUMN_GUID_PROFILES = "guid_profiles";

    /**
     * proposer对应表字段
     */
    public static final String COLUMN_PROPOSER = "proposer";

    /**
     * applyTime对应表字段
     */
    public static final String COLUMN_APPLY_TIME = "apply_time";

    /**
     * deliver对应表字段
     */
    public static final String COLUMN_DELIVER = "deliver";

    /**
     * deliveryTime对应表字段
     */
    public static final String COLUMN_DELIVERY_TIME = "delivery_time";

    /**
     * deliveryResult对应表字段
     */
    public static final String COLUMN_DELIVERY_RESULT = "delivery_result";

    /**
     * deliveryDesc对应表字段
     */
    public static final String COLUMN_DELIVERY_DESC = "delivery_desc";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * applyAlias逻辑名
     */
    public static final String NAME_APPLY_ALIAS = "申请别名";

    /**
     * guidWorkitem逻辑名
     */
    public static final String NAME_GUID_WORKITEM = "工作项GUID";

    /**
     * guidProfiles逻辑名
     */
    public static final String NAME_GUID_PROFILES = "运行环境GUID";

    /**
     * proposer逻辑名
     */
    public static final String NAME_PROPOSER = "投放申请人";

    /**
     * applyTime逻辑名
     */
    public static final String NAME_APPLY_TIME = "提出申请时间";

    /**
     * deliver逻辑名
     */
    public static final String NAME_DELIVER = "投放处理人";

    /**
     * deliveryTime逻辑名
     */
    public static final String NAME_DELIVERY_TIME = "投放时间";

    /**
     * deliveryResult逻辑名
     */
    public static final String NAME_DELIVERY_RESULT = "投放结果";

    /**
     * deliveryDesc逻辑名
     */
    public static final String NAME_DELIVERY_DESC = "投放说明";

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
    private Integer guidWorkitem;

    /**
     * 运行环境GUID:唯一标示某条数据（自增长）
     */
    private Integer guidProfiles;

    /**
     * 投放申请人:提出投放申请的开发人员
     */
    private String proposer;

    /**
     * 提出申请时间
     */
    private Date applyTime;

    /**
     * 投放处理人:谁处理了这个投放申请，一般记录RCT小组成员
     */
    private String deliver;

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
    private String deliveryResult;

    /**
     * 投放说明:对投放结果的说明，如：因为合并代码与申请中投放代码数量不符，RCT投放失败，此处说明该原因
     */
    private String deliveryDesc;

}

