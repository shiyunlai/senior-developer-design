package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

public enum ItemStatus implements BaseEnum {

    DEVELOP("0","开发中"),
    STARTUP("1","已投产"),
    CANCEL("2","已取消");

    private final String value;

    private final String name;

    ItemStatus(final String value, final String name) {
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
