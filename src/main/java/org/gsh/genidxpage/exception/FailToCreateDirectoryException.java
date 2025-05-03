package org.gsh.genidxpage.exception;

import org.gsh.genidxpage.common.exception.ErrorCode;

import java.io.IOException;

public class FailToCreateDirectoryException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public FailToCreateDirectoryException(IOException e, ErrorCode errorCode, String message) {
        super(errorCode.getReason(), e);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
