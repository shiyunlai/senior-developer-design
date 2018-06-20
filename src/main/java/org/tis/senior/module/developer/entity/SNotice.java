package org.tis.senior.module.developer.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * sNotice记录开发过程中，各工作项流转过程中的各种通知记录
 * 
 * @author Auto Generate Tools
 * @date 2018/06/19
 */
@Data
@TableName("s_notice")
public class SNotice implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称
     */
    public static final String NAME = "通知记录";

    /**
     * guid对应表字段
     */
    public static final String COLUMN_GUID = "guid";

    /**
     * eventDesc对应表字段
     */
    public static final String COLUMN_EVENT_DESC = "event_desc";

    /**
     * sender对应表字段
     */
    public static final String COLUMN_SENDER = "sender";

    /**
     * toWho对应表字段
     */
    public static final String COLUMN_TO_WHO = "to_who";

    /**
     * batchNo对应表字段
     */
    public static final String COLUMN_BATCH_NO = "batch_no";

    /**
     * status对应表字段
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * noticeObjGuids对应表字段
     */
    public static final String COLUMN_NOTICE_OBJ_GUIDS = "notice_obj_guids";

    /**
     * guid逻辑名
     */
    public static final String NAME_GUID = "数据id";

    /**
     * eventDesc逻辑名
     */
    public static final String NAME_EVENT_DESC = "事件信息";

    /**
     * sender逻辑名
     */
    public static final String NAME_SENDER = "发出人";

    /**
     * toWho逻辑名
     */
    public static final String NAME_TO_WHO = "给谁的消息";

    /**
     * batchNo逻辑名
     */
    public static final String NAME_BATCH_NO = "消息批次号";

    /**
     * status逻辑名
     */
    public static final String NAME_STATUS = "消息状态";

    /**
     * noticeObjGuids逻辑名
     */
    public static final String NAME_NOTICE_OBJ_GUIDS = "通知关联的对象guid";

    /**
     * 数据id:唯一标示某条数据（自增长）
     */
    @TableId
    private Integer guid;

    /**
     * 事件信息:事件描述，由事件的发起点记录
     * 如：提交代码、投放申请、合并代码、功能项已投产
     */
    private String eventDesc;

    /**
     * 发出人
     */
    private String sender;

    /**
     * 给谁的消息:如果一个事件要通知多人，会生成多条记录
     */
    private String toWho;

    /**
     * 消息批次号:通过batch_no标记发送多个人的同一条消息，系统为每个接收人生成一条消息
     */
    private String batchNo;

    /**
     * 消息状态:0 新生成
     * S 已发送
     * B 已查看（在本系统中点击后，置状态）
     */
    private String status;

    /**
     * 通知关联的对象guid:如：开发人员提出投放申请后，发送通知给RCT人员，则此字段记录工作项、投放申请 两个GUID。
     * 假设以json的方式，记录清楚本次通知中需要告知的对象信息
     * [
     *    {“guid”:”123124”, “obj”:”s_branch”},
     *    {“guid”:”123123”, “obj”:”s_branch”},
     * ]
     */
    private String noticeObjGuids;

}

