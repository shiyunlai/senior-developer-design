package org.tis.senior.module.core.entity.enums;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * description: 自定义枚举类反序列化
 *
 * @author zhaoch
 * @date 2018/4/13
 **/
public class CommonEnumDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            if (BaseEnum.class.isAssignableFrom(Class.forName(type.getTypeName()))) {
                String value = parser.getLexer().stringVal();
                Class<?> enumClass = null;
                try {
                    enumClass = Class.forName(type.getTypeName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                BaseEnum[] ordinalEnums = (BaseEnum[]) enumClass.getEnumConstants();
                for (int i = 0; i < ordinalEnums.length; ++i) {
                    BaseEnum e = ordinalEnums[i];
                    if (StringUtils.equals(e.deserialze().toString(), value)) {
                        return (T) e;
                    }
                }
            } else {
                throw new RuntimeException("该枚举类没有实现实现BaseEnum接口！");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
