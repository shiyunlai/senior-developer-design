package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description: 投产清单来源类型
 * 清单来源：
 * A 手动补录
 * B 分支提交
 * M 合并添加;
 * @author zhaoch
 * @date 2018/7/12
 **/
public enum  DeliveryListFromType implements BaseEnum {

    ADDITION("A", "手动补录"),

    BRANCH("B", "分支提交"),

    STANDARD("S", "标准清单"),

    MERGE("M", "合并添加");

    private final String value;

    private final String name;

    DeliveryListFromType(final String value, final String name) {
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
