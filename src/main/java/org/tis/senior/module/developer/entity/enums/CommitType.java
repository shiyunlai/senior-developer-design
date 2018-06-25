package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description: SVN提交类型
 *
 * @author zhaoch
 * @date 2018/6/24
 **/
public enum CommitType implements BaseEnum {

    ADDED("A", "新增"),

    DELETED("D", "删除"),

    MODIFIED("M", "修改"),

    REPLACED("R", "替换");

    private final String value;

    private final String name;

    CommitType(final String value, final String name) {
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
