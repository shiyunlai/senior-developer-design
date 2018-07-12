package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

public enum BranchMappingStatus implements BaseEnum {

    TAKE("1", "生效"),
    NOTTAKE("0", "不生效");

    private final String value;

    private final String name;

    BranchMappingStatus(final String value, final String name) {
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
