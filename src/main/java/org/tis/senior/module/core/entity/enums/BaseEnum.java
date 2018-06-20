package org.tis.senior.module.core.entity.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * description: 实体类中有枚举类的都要实现该接口
 *
 * @author zhaoch
 * @date 2018/4/16
 **/
public interface BaseEnum extends IEnum {

    /**
     * 反序列化时要匹配的值，如枚举类 Sex(String value, String desc) 有 MALE("n", “男”)，反序列化时，传入字段为“n”,需要转换为Sex.MALE
     * 将Sex类实现该接口，deserialze()方法返回value.
     * @return
     */
    Serializable deserialze();

}
