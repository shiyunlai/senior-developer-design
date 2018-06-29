package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

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

    WAIT("0", "待确认"),

    CONFIRM("1", "确认"),

    DISCUSS("2", "有异议");

    private final String value;

    private final String name;

    ConfirmStatus(final String value, final String name) {
        this.value = value;
        this.name = name;
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
