public enum ErrorCode {
    SUCCESS(0),
    AUTHENTICATION_FAILED(1),
    FILE_NOT_FOUND(2),
    FILE_ALREADY_EXISTS(3),
    INVALID_OPERATION(4),
    INTERNAL_SERVER_ERROR(5),
    NOT_AUTHENTICATED(6),
    UNKNOWN_ERROR(999);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }
}