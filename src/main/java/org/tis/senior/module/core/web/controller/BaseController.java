package org.tis.senior.module.core.web.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.SecurityUtils;
import org.tis.senior.module.core.web.vo.PageVO;
import org.tis.senior.module.core.web.vo.SmartPage;
import org.tis.senior.module.developer.entity.SSvnAccount;
import org.tis.senior.module.developer.util.DeveloperUtils;

import java.lang.reflect.Field;

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

    /**
     * 获取登录用户
     * @return
     */
    protected SSvnAccount getUser() {
        SSvnAccount principal = (SSvnAccount) SecurityUtils.getSubject().getPrincipal();
        principal.setSvnPwd(null);
        return principal;
    }

    protected <S> EntityWrapper<S> getWrapper(S s) {
        if(s == null){
            return null;
        }
        EntityWrapper<S> wrapper = new EntityWrapper<>();
        Field[] fields = s.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                String name = field.getName();
                try {
                    Object o = field.get(s);
                    if (o != null) {
                        wrapper.like(DeveloperUtils.humpToUnderline(name).toLowerCase(), o.toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return wrapper;
    }

}
