package org.tis.senior.module.developer.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;
import org.tis.senior.module.core.entity.enums.CommonEnumDeserializer;
import org.tis.senior.module.developer.entity.enums.IsAllowDelivery;

@Data
public class ProfileBranchDetail {

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
    private String profilesName;

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

    /**
     * 分支路径
     */
    private String fullPath;
}
