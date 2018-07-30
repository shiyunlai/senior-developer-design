package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
public enum BranchType implements BaseEnum {
    /**
     * F 特性分支，普通功能/项目对一个的分支
     * H hot分支，修复生产bug，或开发紧急投产内容的分支
     * R release分支
     */
    FEATURE("F", "feature"),
    HOT("H", "hotfix"),
    RELEASE("R", "release");

    private final String value;

    private final String name;

    BranchType(final String value, final String name) {
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
