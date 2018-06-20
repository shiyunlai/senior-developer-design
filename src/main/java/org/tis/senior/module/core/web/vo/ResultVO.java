package org.tis.senior.module.core.web.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tis.senior.module.core.entity.response.RestResponse;

/**
 * describe: 请求响应格式
 *
 * @author zhaoch
 * @date 2018/4/3
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ResultVO extends RestResponse {

    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_CODE = "500";

    private String code;

    private String msg;

    private Object result;

    public ResultVO() {
    }

    public ResultVO(String code) {
        this.code = code;
    }

    public ResultVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(String code, Object result) {
        this.code = code;
        this.result = result;
    }
    public ResultVO(String code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ResultVO(Object result) {
        this.result = result;
    }

    public static ResultVO error() {
        return error("500", "未知异常，请联系管理员");
    }

    public static ResultVO error(String msg) {
        return error("500", msg);
    }

    public static ResultVO error(String code, String msg) {
        return new ResultVO(code, msg, null);
    }

    public static ResultVO success() {
        return new ResultVO(SUCCESS_CODE);
    }

    public static ResultVO success(String msg) {
        return new ResultVO(SUCCESS_CODE, msg);
    }
    public static ResultVO success(Object result) {
        return new ResultVO(SUCCESS_CODE, null, result);
    }

    public static ResultVO success(String msg, Object result) {
        return new ResultVO(SUCCESS_CODE, msg, result);
    }


    public static ResultVO failure(String code, String msg) {
        return new ResultVO(code, msg);
    }




}
