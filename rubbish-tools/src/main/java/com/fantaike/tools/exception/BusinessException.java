package com.fantaike.tools.exception;

public class BusinessException extends Exception {
	private static final long serialVersionUID = -721570378388704423L;

	private BusinessException(String message) {
		super(message);
	}

	public static BusinessException getException(String message, Exception exception) {
		if (exception instanceof BusinessException) {
			return (BusinessException) exception;
		}
		return new BusinessException(message);
	}

	public static BusinessException getException(String message) {
		return new BusinessException(message);
	}

}
