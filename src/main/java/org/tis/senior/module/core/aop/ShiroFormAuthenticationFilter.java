package org.tis.senior.module.core.aop;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.tis.senior.module.core.web.vo.ResultVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/29
 **/
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取当前登录
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            // 使用response响应流返回数据到前台（因前端需要接受json数据，注意前后端跨域问题）
            String jsonString = JSON.toJSONString(ResultVO.error("AUTH-401", "尚未登录或登录失效，请重新登录！"));
            ((HttpServletResponse)response).setHeader("Access-Control-Allow-Origin",
                    ((HttpServletRequest) request).getHeader("Origin"));
            ((HttpServletResponse)response).setHeader("Access-Control-Allow-Headers", "Content-Type,token,Authorization");
            ajax((HttpServletResponse) response, jsonString);
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = super.isAccessAllowed(request, response, mappedValue);
        if (!allowed) {
            // 判断请求是否是options请求
            String method = WebUtils.toHttp(request).getMethod();
            if (StringUtils.equalsIgnoreCase("OPTIONS", method)) {
                return true;
            }
        }
        return allowed;

    }

    private void ajax(HttpServletResponse response, String content) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.getWriter().write(content);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
}
