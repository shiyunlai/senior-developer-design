package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

public enum ProjectType implements BaseEnum {

    SPECIAL("S", "普通工程"),
    COMMON("C", "特殊工程");

    private final String value;

    private final String name;

    ProjectType(final String value, final String name) {
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