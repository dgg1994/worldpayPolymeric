package com.polymeric.exception;

/**
 * 基础异常类
 *
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -8458116729669426482L;

	public BaseException() {
		super();
	}

	public BaseException(String message, Throwable cause,
                         boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

}
