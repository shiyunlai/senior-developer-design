package org.tis.senior.module.core.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tis.senior.module.core.exception.WebAppException;
import org.tis.senior.module.core.web.vo.ResultVO;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Set;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author
 * @date 2018/04/18
 */
public class BaseControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 用于处理通用异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "数据验证失败:";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + ", ";
        }
        return ResultVO.error("400", errorMesssage);
    }

    /**
     * 用于处理通用异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO methodValidateException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMesssage = "参数验证失败:";
        for (ConstraintViolation<?> violation : violations) {
            errorMesssage += violation.getMessage();
        }
        return ResultVO.error("400", errorMesssage);
    }

    /**
     * 用于处理通用异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResultVO messageConversionException(HttpMessageConversionException e) {
        return ResultVO.error("400", "请求数据不合法！");
    }

    /**
     * WebApp层异常
     */
    @ExceptionHandler(WebAppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleWebAppException(WebAppException ex) {
        return ResultVO.error(ex.getCode(), ex.getMessage());
    }


    /**
     * SQL错误
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleSQLException() {
        // SQL堆栈信息包含敏感信息，不返回前端，在日志中可以查看详细信息
        return ResultVO.error("内部服务发生了SQL错误！");
    }

    /**
     * SQL错误
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleDataAccessException() {
        // SQL堆栈信息包含敏感信息，不返回前端，在日志中可以查看详细信息
        return ResultVO.error("内部服务发生了SQL错误！");
    }

    /**
     * 未知异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleUnexpectedServerError(Exception ex) {
        return ResultVO.error(ex.getMessage());
    }

}
