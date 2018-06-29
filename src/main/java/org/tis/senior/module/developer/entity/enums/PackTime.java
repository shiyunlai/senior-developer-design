package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;
import org.tis.senior.module.developer.exception.DeveloperException;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
public enum PackTime implements BaseEnum {

    T0900("09:00"),
    T1200("12:00"),
    T1700("17:00");

    private final String value;

    PackTime(final String value) {
        this.value = value;
    }

    public static PackTime what(String value) {
        for (PackTime packTime : PackTime.values()) {
            if (packTime.getValue().equals(value)) {
                return packTime;
            }
        }
        throw new DeveloperException("没有匹配的窗口！");
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
        return value;
    }
}
