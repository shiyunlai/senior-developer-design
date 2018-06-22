package org.tis.senior.module.developer.exception;

/**
 * description:
 *
 * @author zhaoch
 * @date 2018/6/22
 **/
public class DeveloperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DeveloperException(String message) {
        super(message);
    }

    public DeveloperException(String message, Throwable cause) {
        super(message, cause) ;
    }
}
