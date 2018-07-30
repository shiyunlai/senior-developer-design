package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.SqlCondition;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;

import java.io.Serializable;

/**
 * sProfiles记录有哪些验证环境，如：SIT、SIT_DEV、UAT…. PP、生产
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Data
@TableName("s_profiles")
public class SProfiles implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "运行环境";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * profilesCode对应表字段
     */
    public static final String COLUMN_PROFILES_CODE = "profiles_code";

    /**
     * profilesName对应表字段
     */
    public static final String COLUMN_PROFILES_NAME = "profiles_name";

    /**
     * artf对应表字段
     */
    public static final String COLUMN_ARTF = "artf";

    /**
     * hostIp对应表字段
     */
    public static final String COLUMN_HOST_IP = "host_ip";

    /**
     * installPath对应表字段
     */
    public static final String COLUMN_INSTALL_PATH = "install_path";

    /**
     * csvUser对应表字段
     */
    public static final String COLUMN_CSV_USER = "csv_user";

    /**
     * csvPwd对应表字段
     */
    public static final String COLUMN_CSV_PWD = "csv_pwd";

    /**
     * isAllowDelivery对应表字段
     */
    public static final String COLUMN_IS_ALLOW_DELIVERY = "is_allow_delivery";

    /**
     * manager对应表字段
     */
    public static final String COLUMN_MANAGER = "manager";

    /**
     * packTiming对应表字段
     */
    public static final String COLUMN_PACK_TIMING = "pack_timing";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * profilesCode逻辑名
     */
    public static final String NAME_PROFILES_CODE = "环境代码";

    /**
     * profilesName逻辑名
     */
    public static final String NAME_PROFILES_NAME = "环境名称";

    /**
     * artf逻辑名
     */
    public static final String NAME_ARTF = "提交标识";

    /**
     * hostIp逻辑名
     */
    public static final String NAME_HOST_IP = "主机ip";

    /**
     * installPath逻辑名
     */
    public static final String NAME_INSTALL_PATH = "安装路径";

    /**
     * csvUser逻辑名
     */
    public static final String NAME_CSV_USER = "版本控制用户";

    /**
     * csvPwd逻辑名
     */
    public static final String NAME_CSV_PWD = "版本控制密码";

    /**
     * isAllowDelivery逻辑名
     */
    public static final String NAME_IS_ALLOW_DELIVERY = "是否允许投放";

    /**
     * manager逻辑名
     */
    public static final String NAME_MANAGER = "环境管理人员";

    /**
     * packTiming逻辑名
     */
    public static final String NAME_PACK_TIMING = "打包窗口";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 环境代码:建议大写字母，如：:SIT、SIT_DEV、UAT
     */
    private String profilesCode;

    /**
     * 环境名称
     */
    @TableField(condition = SqlCondition.LIKE)
    private String profilesName;

    /**
     * 提交标识
     */
    private String artf;

    /**
     * 主机ip:环境对应的服务器IP地址
     */
    private String hostIp;

    /**
     * 安装路径:指系统所在的安装路径
     */
    private String installPath;

    /**
     * 版本控制用户:如：登录对应分支的svn账号
     */
    private String csvUser;

    /**
     * 版本控制密码
     */
    private String csvPwd;

    /**
     * 是否允许投放:由RCT组人员控制，对开发人员开放投产窗口
     * 1 允许向本环境提交投放申请
     * 0 不允许
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private IsAllowDelivery isAllowDelivery;

    /**
     * 环境管理人员
     */
    private String manager;

    /**
     * 打包窗口:本环境对应的打包窗口时间，用业务字典来实现
     * 如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00
     */
    private String packTiming;

}

