package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sDeliveryList（开发人员提出）投放申请，其中包括哪些程序文件
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_delivery_list")
public class SDeliveryList implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "投产代码清单";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * guidDelivery对应表字段
     */
    public static final String COLUMN_GUID_DELIVERY = "guid_delivery";

    /**
     * guidProgram对应表字段
     */
    public static final String COLUMN_GUID_PROGRAM = "guid_program";

    /**
     * deliveryVersion对应表字段
     */
    public static final String COLUMN_DELIVERY_VERSION = "delivery_version";

    /**
     * patchType对应表字段
     */
    public static final String COLUMN_PATCH_TYPE = "patch_type";

    /**
     * programName对应表字段
     */
    public static final String COLUMN_PROGRAM_NAME = "program_name";

    /**
     * fullPath对应表字段
     */
    public static final String COLUMN_FULL_PATH = "full_path";

    /**
     * partOfProject对应表字段
     */
    public static final String COLUMN_PART_OF_PROJECT = "part_of_project";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * guidDelivery逻辑名
     */
    public static final String NAME_GUID_DELIVERY = "投放申请GUID";

    /**
     * guidProgram逻辑名
     */
    public static final String NAME_GUID_PROGRAM = "程序GUID";

    /**
     * deliveryVersion逻辑名
     */
    public static final String NAME_DELIVERY_VERSION = "投放版本";

    /**
     * patchType逻辑名
     */
    public static final String NAME_PATCH_TYPE = "补丁类型";

    /**
     * programName逻辑名
     */
    public static final String NAME_PROGRAM_NAME = "程序名称";

    /**
     * fullPath逻辑名
     */
    public static final String NAME_FULL_PATH = "代码全路径";

    /**
     * partOfProject逻辑名
     */
    public static final String NAME_PART_OF_PROJECT = "代码所在工程";

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
     * 程序GUID:唯一标示某条数据（自增长）
     */
    private Integer guidProgram;

    /**
     * 投放版本:投放时该代码的svn版本
     * 可以投当前版本，也可以投开发过程中的某个版本
     */
    private Integer deliveryVersion;

    /**
     * 补丁类型:JAR 输出为jar包
     * ECD 输出为ecd包
     * EPD 输出为epd
     * CFG 作为配置文件
     * DBV 作为数据库脚本（SQL、DDL等数据库版本脚本）
     */
    private String patchType;

    /**
     * 程序名称:冗余设计
     */
    private String programName;

    /**
     * 代码全路径:冗余设计
     */
    private String fullPath;

    /**
     * 代码所在工程:记录该代码所在的工程名称（s_project.project_name）
     * 冗余设计
     */
    private String partOfProject;

}

