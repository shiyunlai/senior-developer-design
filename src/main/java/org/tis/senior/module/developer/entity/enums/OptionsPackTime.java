package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

public enum OptionsPackTime  implements BaseEnum {

    YES("Y"),
    DEFALIT("D"),
    NO("N");


    private final String value;

    OptionsPackTime(final String value) {
        this.value = value;
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
