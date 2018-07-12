package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description:核对错误类型
 * D 申请清单异常
 * M 合并清单异常
 *
 * @author zhaoch
 * @date 2018/7/11
 **/
public enum CheckErrorType implements BaseEnum {

    Delivery("D", "申请清单异常"),

    Merge("M", "合并清单异常");

    private final String value;

    private final String name;

    CheckErrorType(final String value, final String name) {
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
