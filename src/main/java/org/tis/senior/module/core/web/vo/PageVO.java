package org.tis.senior.module.core.web.vo;

import javax.validation.constraints.Min;

/**
 * describe: 分页对象
 *
 * @author zhaoch
 * @date 2018/4/3
 **/
public class PageVO {

    /**
     * 当前页
     */
    @Min(value = 1, message = "当前页至少为1")
    private Integer current = 1;

    /**
     * 每页显示条数
     */
    @Min(value = 1, message = "每页至少显示1条")
    private Integer size = 10;

    /**
     * 排序字段
     */
    private String orderByField;

    /**
     * 是否升序
     */
    private Boolean asc = true;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    @Override
    public String toString() {
        return "{" +
                "current=" + current +
                ", size=" + size +
                ", orderByField='" + orderByField + '\'' +
                ", asc=" + asc +
                '}';
    }
}

