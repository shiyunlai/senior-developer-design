package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description: 投放结果
 *
 * @author zhaoch
 * @date 2018/6/21
 **/
public enum DeliveryResult implements BaseEnum {
    /**
     * 投放结果:0 申请中
     * S 成功
     * F 失败
     * C 取消投放（功能没有投放）
     */
    APPLYING("0", "申请中"),
    SUCCESS("S", "成功"),
    FAILED("F", "失败"),
    CANCEL("C", "取消投放");

    private final String value;

    private final String name;

    DeliveryResult(final String value, final String name) {
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

    public boolean isSuccess() {
        return this.equals(SUCCESS);
    }
}
