package org.tis.senior.module.core.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.tis.senior.module.core.web.vo.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * describe: 返回Rest错误信息
 *
 * @author zhaoch
 * @date 2018/4/2
 **/
@RestController
public class RestErrorEndpoint implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello, Senior!";
    }

    @RequestMapping(value = PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO error(HttpServletRequest request, HttpServletResponse response) {

        return buildBody(request, response, true);
//        return buildBody(request,false);

    }

    private ResultVO buildBody(HttpServletRequest request, HttpServletResponse response, Boolean includeStackTrace) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, includeStackTrace);
        Integer status = (Integer) errorAttributes.get("status");
        String path = (String) errorAttributes.get("path");
        String messageFound = (String) errorAttributes.get("message");
        String message = "";
        String trace = "";
        if (!StringUtils.isEmpty(path)) {
            message = String.format("请求[%s]发生错误：%s", path, messageFound);
        }
        if (includeStackTrace) {
            trace = (String) errorAttributes.get("trace");
            if (!StringUtils.isEmpty(trace)) {
                message += String.format(" \nTrace: %s", trace);
            }
        }
        response.setStatus(status);
        return ResultVO.error(String.valueOf(status), message);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}
