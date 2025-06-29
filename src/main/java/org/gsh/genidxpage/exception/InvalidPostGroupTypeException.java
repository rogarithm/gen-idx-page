package org.gsh.genidxpage.exception;

import org.gsh.genidxpage.common.exception.ErrorCode;

public class InvalidPostGroupTypeException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public InvalidPostGroupTypeException(ErrorCode errorCode, String message) {
        super(errorCode.getReason());
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
