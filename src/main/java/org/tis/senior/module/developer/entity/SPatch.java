package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sPatch根据投放申请制作出来的补丁
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Data
@TableName("s_patch")
public class SPatch implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "补丁";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidDelivery对应表字段
     */
    public static final String COLUMN_GUID_DELIVERY = "guid_delivery";

    /**
     * patchTime对应表字段
     */
    public static final String COLUMN_PATCH_TIME = "patch_time";

    /**
     * patcher对应表字段
     */
    public static final String COLUMN_PATCHER = "patcher";

    /**
     * patchName对应表字段
     */
    public static final String COLUMN_PATCH_NAME = "patch_name";

    /**
     * deployTime对应表字段
     */
    public static final String COLUMN_DEPLOY_TIME = "deploy_time";

    /**
     * deployToProfiles对应表字段
     */
    public static final String COLUMN_DEPLOY_TO_PROFILES = "deploy_to_profiles";

    /**
     * deployResult对应表字段
     */
    public static final String COLUMN_DEPLOY_RESULT = "deploy_result";

    /**
     * deployDesc对应表字段
     */
    public static final String COLUMN_DEPLOY_DESC = "deploy_desc";

    /**
     * deployer对应表字段
     */
    public static final String COLUMN_DEPLOYER = "deployer";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidDelivery逻辑名
     */
    public static final String NAME_GUID_DELIVERY = "投放申请GUID";

    /**
     * patchTime逻辑名
     */
    public static final String NAME_PATCH_TIME = "补丁制作时间";

    /**
     * patcher逻辑名
     */
    public static final String NAME_PATCHER = "补丁制作人";

    /**
     * patchName逻辑名
     */
    public static final String NAME_PATCH_NAME = "补丁名称";

    /**
     * deployTime逻辑名
     */
    public static final String NAME_DEPLOY_TIME = "部署时间";

    /**
     * deployToProfiles逻辑名
     */
    public static final String NAME_DEPLOY_TO_PROFILES = "部署到什么环境";

    /**
     * deployResult逻辑名
     */
    public static final String NAME_DEPLOY_RESULT = "部署结果";

    /**
     * deployDesc逻辑名
     */
    public static final String NAME_DEPLOY_DESC = "部署说明";

    /**
     * deployer逻辑名
     */
    public static final String NAME_DEPLOYER = "部署人";

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
     * 补丁制作时间
     */
    private Date patchTime;

    /**
     * 补丁制作人
     */
    private String patcher;

    /**
     * 补丁名称
     */
    private String patchName;

    /**
     * 部署时间:补丁被投放到对应环境的时间
     */
    private Date deployTime;

    /**
     * 部署到什么环境:补丁部署到了哪个环境，则存储该环境的GUID
     */
    private Integer deployToProfiles;

    /**
     * 部署结果:0 init待部署（默认，补丁制作好后初始状态为0-init待部署）
     * 1 部署成功
     * 2 部署失败
     * C 取消部署
     */
    private String deployResult;

    /**
     * 部署说明:对部署状态的描述
     */
    private String deployDesc;

    /**
     * 部署人
     */
    private String deployer;

}

