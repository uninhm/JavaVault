public record Result<T>(ErrorCode errorCode, T data) {
    public Result(ErrorCode errorCode) {
        this(errorCode, null);
    }
}