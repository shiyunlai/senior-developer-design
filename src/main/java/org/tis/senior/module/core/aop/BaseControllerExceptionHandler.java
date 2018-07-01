package org.tis.senior.module.core.aop;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tis.senior.module.core.exception.WebAppException;
import org.tis.senior.module.core.web.vo.ResultVO;
import org.tis.senior.module.developer.exception.DeveloperException;
import org.tmatesoft.svn.core.SVNException;

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
@RestControllerAdvice
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
        return ResultVO.error("400", "请求数据不合法！" + e.getMessage());
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
    public ResultVO handleSQLException(SQLException e) {
        // SQL堆栈信息包含敏感信息，不返回前端，在日志中可以查看详细信息
        e.printStackTrace();
        return ResultVO.error("内部服务发生了SQL错误！");
    }

    /**
     * SQL错误
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleDataAccessException(DataAccessException e) {
        // SQL堆栈信息包含敏感信息，不返回前端，在日志中可以查看详细信息
        e.printStackTrace();
        return ResultVO.error("内部服务发生了SQL错误！");
    }


    /**
     * 权限异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResultVO handleShiroException(ShiroException ex) {
        String msg ;
        String code;
        if (ex instanceof IncorrectCredentialsException) {
            code = "AUTH-440";
            msg = "用户名或密码错误！";
        } else if (ex instanceof ExcessiveAttemptsException) {
            code = "AUTH-445";
            msg = "达到最大错误次数，请联系管理员或稍后再试！";
        } else if (ex instanceof UnauthenticatedException || ex instanceof AuthenticationException) {
            code = "AUTH-401";
            msg = "尚未登录或登录失效，请重新登录！";
        } else if (ex instanceof UnauthorizedException || ex instanceof AuthorizationException) {
            code = "AUTH-403";
            msg = "权限不足！";
        } else {
            code = "AUTH-444";
            msg = StringUtils.isBlank(ex.getMessage()) ? ex.getMessage() : ex.getCause().getMessage();
        }
        return ResultVO.failure(code, msg);
    }

    @ExceptionHandler(DeveloperException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO handleDeveloperException(DeveloperException ex) {
        return ResultVO.failure("400", "错误的请求！" + ex.getMessage());
    }

    /**
     * SVN异常
     * @param ex
     * @return
     */
    @ExceptionHandler(SVNException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleSVNException(SVNException ex) {
        ex.printStackTrace();
        return ResultVO.error("SVN异常！" + ex.getMessage());
    }

    /**
     * 未知异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO handleUnexpectedServerError(Exception ex) {
        ex.printStackTrace();
        return ResultVO.error("内部错误！" + ex.getMessage());
    }

}
