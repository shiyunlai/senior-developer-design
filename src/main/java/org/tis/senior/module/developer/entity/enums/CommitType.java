package org.tis.senior.module.developer.entity.enums;

import org.apache.commons.lang3.StringUtils;
import org.tis.senior.module.core.entity.enums.BaseEnum;
import org.tis.senior.module.developer.exception.DeveloperException;

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

    public static CommitType what(String value) {
        if (StringUtils.isBlank(value)) {
            throw new DeveloperException("提交类型不能为null！");
        }
        if("added".equals(value)){
            return ADDED;
        }
        for (CommitType type : CommitType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new DeveloperException("没有" + value + "对应的提交类型！");
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
