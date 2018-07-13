package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 投放结果
 * 0 申请中
 * S 成功
 * F 失败
 * C 核对中（功能没有投放）
 * @author zhaoch
 * @date 2018/6/21
 **/
public enum DeliveryResult implements BaseEnum {

    APPLYING("0", "申请中"),
    MERGED("M", "已合并"),
    CHECKING("C", "核对中"),
    SUCCESS("S", "核对成功"),
    FAILED("F", "核对失败"),
    DELIVERED("D", "投放成功");

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

    public static List<String> unfinished() {
        List<String> list = new ArrayList<>();
        list.add(DeliveryResult.APPLYING.getValue().toString());
        list.add(DeliveryResult.MERGED.getValue().toString());
        list.add(DeliveryResult.CHECKING.getValue().toString());
        list.add(DeliveryResult.SUCCESS.getValue().toString());
        return list;
    }
}
