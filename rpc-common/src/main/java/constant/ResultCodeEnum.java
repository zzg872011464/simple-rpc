package constant;

public enum ResultCodeEnum {
    OK(1,"OK"),
    FAIL(2,"FAIL");

    private final Integer resultCode;
    private final String message;

    ResultCodeEnum(Integer resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
