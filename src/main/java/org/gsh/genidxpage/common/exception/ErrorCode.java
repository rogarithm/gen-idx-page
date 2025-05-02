package org.gsh.genidxpage.common.exception;

public enum ErrorCode {

    BAD_REQUEST("400", "잘못된 요청"),
    SERVER_FAULT("500", "서버 오류");

    private final String code;
    private final String reason;

    ErrorCode(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public String getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }
}
