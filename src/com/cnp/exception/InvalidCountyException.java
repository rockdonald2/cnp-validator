package com.cnp.exception;

public class InvalidCountyException extends CnpException {

	public InvalidCountyException(String errorMsg) {
		super(errorMsg, ErrorCode.INVALID_CNP);
	}

}
