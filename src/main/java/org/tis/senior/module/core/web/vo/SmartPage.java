package org.tis.senior.module.core.web.vo;

import org.tis.senior.module.core.web.vo.PageVO;

import javax.validation.constraints.NotNull;

/**
 * describe: 分页请求对象
 *
 * @author zhaoch
 * @date 2018/4/3
 **/
public class SmartPage<T> {

    @NotNull(message = "分页参数不能为空")
    private PageVO page;

    private T condition;

    public PageVO getPage() {
        return page;
    }

    public void setPage(PageVO page) {
        this.page = page;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }
}
