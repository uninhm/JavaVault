public enum OperationCode {
    AUTHENTICATE(0),
    SEND_FILE(1),
    GET_FILE(2),
    DELETE_FILE(3),
    LIST_FILES(4);

    private final int code;

    OperationCode(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
