package org.tis.senior.module.core.web.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.tis.senior.module.core.web.vo.PageVO;
import org.tis.senior.module.core.web.vo.SmartPage;

/**
 * describe: 
 *
 * @author zhaoch
 * @date 2018/3/27
 **/
public class BaseController<T> {

    protected Page<T> getPage(SmartPage<T> smartPage) {
        PageVO vo = smartPage.getPage();
        Page<T> page = new Page<>(vo.getCurrent(), vo.getSize());
        if (vo.getOrderByField() != null) {
            page.setOrderByField(vo.getOrderByField());
            page.setAsc(vo.getAsc());
        }
        return page;
    }

    protected EntityWrapper<T> getCondition(SmartPage<T> smartPage) {
        T condition = smartPage.getCondition();
        if (condition == null) {
            return null;
        }
        return new EntityWrapper<>(condition);
    }




}
