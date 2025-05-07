package org.gsh.genidxpage.exception;

import org.gsh.genidxpage.common.exception.ErrorCode;

import java.io.IOException;

public class FailToWriteFileException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public FailToWriteFileException(IOException e, ErrorCode errorCode, String message) {
        super(errorCode.getReason(), e);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
