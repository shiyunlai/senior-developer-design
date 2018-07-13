package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;
import org.tis.senior.module.developer.exception.DeveloperException;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/28
 **/
public enum CheckStatus implements BaseEnum {
    /**
     * 核对状态
     */
    FAILURE("F", "核对作废"),

    SUCCESS("S", "核对完成"),

    WAIT("W", "正在核对");

    private final String value;

    private final String name;

    CheckStatus(final String value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static CheckStatus what(String value) {
        for (CheckStatus status : CheckStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new DeveloperException("没有" + value + "对应的核对状态！");
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
