package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;
import org.tis.senior.module.developer.exception.DeveloperException;

import java.io.Serializable;

/**
 * description: 开发确认:合并后的代码都需要开发人员进行确认
 * 0 待确认
 * 1 确认
 * 2 有异议（代码合并有问题，需要线下手工处理）
 *
 * @author zhaoch
 * @date 2018/6/28
 **/
public enum ConfirmStatus implements BaseEnum {
    /**
     * 初始状态
     */
    WAIT("0", "待确认"),

    /**
     * 已合并：
     *      RCT人员与开发确认，RCT补合并了代码，点击“已合并”，
     *      增加了发版分支中的代码记录，并且此标记该代码需要投放，会进入“标准清单”；
     */
    MERGED("1", "已合并"),

    /**
     * 不投放：
     *      RCT人员与开发确认清楚，确认此代码不用投产，点击“不投产”，标示该代码不用投放，不会进入“标准清单”
     */
    UNDELIVERY("2", "不投放"),

    /**
     * 删除:
     * RCT人员与开发确认清楚，此为无效代码时，点删除，以此标记不用投放，不会进入“标准清单”
     */
    DELETE("3", "删除"),

    /**
     * 加入投放:
     * RCT与开发人员确认后，此代码为投放申请时少了，点击加入投放，以此标记需要加入投放，会进入“标准清单”
     */
    DELIVERY("4", "加入投放");


    private final String value;

    private final String name;

    ConfirmStatus(final String value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static ConfirmStatus what(String value) {
        for (ConfirmStatus status : ConfirmStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new DeveloperException("没有" + value + "对应的确认状态！");
    }

    @Override
    public Serializable deserialze() {
        return value;
    }

    @Override
    public Serializable getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
