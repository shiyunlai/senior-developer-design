package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.tis.senior.module.core.entity.cascade.CommonCascadeSerializer;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.CheckStatus;
import org.tis.senior.module.developer.entity.enums.PackTime;

import java.io.Serializable;
import java.util.Date;

/**
 * sCheck
 * 
 * @author Auto Generate Tools
 * @date 2018/06/27
 */
@Data
@TableName("s_check")
public class SCheck implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "投产核对";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * checkAlias对应表字段
     */
    public static final String COLUMN_CHECK_ALIAS = "check_alias";

    /**
     * guidProfiles对应表字段
     */
    public static final String COLUMN_GUID_PROFILES = "guid_profiles";

    /**
     * packTiming对应表字段
     */
    public static final String COLUMN_PACK_TIMING = "pack_timing";

    /**
     * checkDate对应表字段
     */
    public static final String COLUMN_CHECK_DATE = "check_date";

    /**
     * checkStatus对应表字段
     */
    public static final String COLUMN_CHECK_STATUS = "check_status";

    /**
     * checkUser对应表字段
     */
    public static final String COLUMN_CHECK_USER = "check_user";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * checkAlias逻辑名
     */
    public static final String NAME_CHECK_ALIAS = "核对别名";

    /**
     * guidProfiles逻辑名
     */
    public static final String NAME_GUID_PROFILES = "运行环境GUID";

    /**
     * packTiming逻辑名
     */
    public static final String NAME_PACK_TIMING = "打包窗口";

    /**
     * checkDate逻辑名
     */
    public static final String NAME_CHECK_DATE = "核对时间";

    /**
     * checkStatus逻辑名
     */
    public static final String NAME_CHECK_STATUS = "核对状态";

    /**
     * checkUser逻辑名
     */
    public static final String NAME_CHECK_USER = "核对人员";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 核对别名:每次核对后由服务端生成，如201806270900第一次核对，日期+窗口+第N次核对
     */
    private String checkAlias;

    /**
     * 运行环境GUID:唯一标示某条数据（自增长）
     */
    @JSONField(serializeUsing = CommonCascadeSerializer.class)
    private Integer guidProfiles;

    /**
     * 打包窗口
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private PackTime packTiming;

    /**
     * 核对时间
     */
    private Date checkDate;

    /**
     * 核对状态:F 核对错误
     * S 核对成功
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private CheckStatus checkStatus;

    /**
     * 核对人员
     */
    private String checkUser;

}

