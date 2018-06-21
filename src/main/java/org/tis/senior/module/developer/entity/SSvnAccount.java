package org.tis.senior.module.developer.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.SvnRole;

import java.io.Serializable;

/**
 * sSvnAccount
 * 
 * @author Auto Generate Tools
 * @date 2018/06/20
 */
@Data
@TableName("s_svn_account")
public class SSvnAccount implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "svn账号";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * userId对应表字段
     */
    public static final String COLUMN_USER_ID = "user_id";

    /**
     * svnUser对应表字段
     */
    public static final String COLUMN_SVN_USER = "svn_user";

    /**
     * svnPwd对应表字段
     */
    public static final String COLUMN_SVN_PWD = "svn_pwd";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * userId逻辑名
     */
    public static final String NAME_USER_ID = "用户";

    /**
     * svnUser逻辑名
     */
    public static final String NAME_SVN_USER = "svn账号";

    /**
     * svnPwd逻辑名
     */
    public static final String NAME_SVN_PWD = "svn密码";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 用户:登录用户
     */
    private String userId;

    /**
     * svn账号
     */
    private String svnUser;

    /**
     * svn密码
     */
    private String svnPwd;
    /**
     * svn角色
     */
    @JSONField(deserializeUsing = CommonEnumDeserializer.class)
    private SvnRole role;

}

