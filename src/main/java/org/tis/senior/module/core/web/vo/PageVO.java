package org.tis.senior.module.core.web.vo;
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
    private Integer current = 0;

    /**
     * 每页显示条数
     */
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

