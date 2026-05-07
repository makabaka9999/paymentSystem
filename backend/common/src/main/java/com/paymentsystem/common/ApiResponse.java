package com.paymentsystem.common;

/**
 * 统一接口响应包装对象。
 *
 * @param <T> 响应数据类型
 */
public class ApiResponse<T> {
    /** 请求是否处理成功。 */
    private boolean success;
    /** 业务响应码。 */
    private String code;
    /** 业务响应消息。 */
    private String message;
    /** 响应数据。 */
    private T data;

    /**
     * 创建空响应对象，供序列化框架使用。
     */
    public ApiResponse() {
    }

    /**
     * 创建完整响应对象。
     */
    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建成功响应。
     *
     * @param data 响应数据
     * @return 成功响应
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(true, "OK", "success", data);
    }

    /**
     * 创建已受理响应。
     *
     * @param data 响应数据
     * @return 已受理响应
     */
    public static <T> ApiResponse<T> accepted(T data) {
        return new ApiResponse<T>(true, "ACCEPTED", "accepted", data);
    }

    /**
     * 创建失败响应。
     *
     * @param code 业务错误码
     * @param message 业务错误消息
     * @return 失败响应
     */
    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<T>(false, code, message, null);
    }

    /** @return 请求是否处理成功 */
    public boolean isSuccess() {
        return success;
    }

    /** @return 业务响应码 */
    public String getCode() {
        return code;
    }

    /** @return 业务响应消息 */
    public String getMessage() {
        return message;
    }

    /** @return 响应数据 */
    public T getData() {
        return data;
    }
}
