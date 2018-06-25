package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description: 为何而创建分支:
 * W  为开发项（Workitem）
 * R  为运行环境发版（Release）
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
public enum BranchForWhat implements BaseEnum {

    WORKITEM("W", "开发项"),
    RELEASE("R", "运行环境发版");

    private final String value;

    private final String name;

    BranchForWhat(final String value, final String name) {
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
