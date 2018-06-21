package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/21
 **/
public enum SvnRole implements BaseEnum {

    RCT("rct", "RCT小组"),
    DEV("dev", "开发组"),
    ADMIN("admin", "管理员");

    private final String value;

    private final String name;

    SvnRole(final String value, final String name) {
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
