package org.gsh.genidxpage.exception;

import org.gsh.genidxpage.common.exception.ErrorCode;

import java.io.IOException;

public class FailToReadRequestInputFileException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public FailToReadRequestInputFileException(IOException e, ErrorCode errorCode, String message) {
        super(errorCode.getReason(), e);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
