package org.tis.senior.module.developer.entity.enums;

import org.tis.senior.module.core.entity.enums.BaseEnum;

import java.io.Serializable;

/**
 * description:
 * JAR 输出为jar包
 * ECD 输出为ecd包
 * EPD 输出为epd
 * CFG 作为配置文件
 * DBV 作为数据库脚本（SQL、DDL等数据库版本脚本）
 *
 * @author zhaoch
 * @date 2018/6/24
 **/
public enum PatchType implements BaseEnum {

    ECD("ecd"),
    EPD("epd"),
    JAR("jar"),
    EXE_JAR("可执行jar"),
    PLUGINS("plugins"),
    DBV("sql"),
    CFG("配置文件");

    private final String value;

    PatchType(final String value) {
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
