package com.cnp.exception;

public class InvalidCenturyException extends CnpException {

	public InvalidCenturyException(String errorMsg) {
		super(errorMsg, ErrorCode.INVALID_CNP);
	}

}
