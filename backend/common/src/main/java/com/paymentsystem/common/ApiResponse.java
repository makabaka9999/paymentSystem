package com.paymentsystem.common;

public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, "OK", "success", data);
    }

    public static <T> ApiResponse<T> accepted(T data) {
        return new ApiResponse<T>(true, "ACCEPTED", "accepted", data);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<T>(false, code, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
