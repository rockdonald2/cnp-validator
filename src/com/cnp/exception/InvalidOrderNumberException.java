package com.cnp.exception;

public class InvalidOrderNumberException extends CnpException {

	public InvalidOrderNumberException(String errorMsg) {
		super(errorMsg, ErrorCode.INVALID_CNP);
	}

}
