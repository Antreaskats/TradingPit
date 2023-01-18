package com.tradingpit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CallFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2709146907258161848L;

	public CallFailedException(String message) {
		super(message);
	}

}
