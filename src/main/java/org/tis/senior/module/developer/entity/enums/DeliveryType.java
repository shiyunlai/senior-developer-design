package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/21
 **/
public enum DeliveryType implements BaseEnum {
    /**
     * 投放类型：G 普通申请
     * M 合并申请
     */
    GENERAL("G", "普通申请"),
    MERGE("M", "合并申请");

    private final String value;

    private final String name;

    DeliveryType(final String value, final String name) {
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
