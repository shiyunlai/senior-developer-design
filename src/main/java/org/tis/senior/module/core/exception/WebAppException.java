/**
 * 
 */
package org.tis.senior.module.core.exception;

/**
 * 
 * webapp层异常类
 * 
 * @author megapro
 *
 */
public class WebAppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	public WebAppException(String code, String message) {
		super(message);
		this.code = code;
	}

	public WebAppException(String code, String message, Throwable t) {
		super(message,t);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
