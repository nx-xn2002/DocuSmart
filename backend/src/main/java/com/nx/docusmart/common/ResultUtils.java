package com.nx.docusmart.common;

/**
 * 返回工具类
 *
 * @author nx-xn2002
 * @date 2025-01-11
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data data
     * @return {@link BaseResponse }<{@link T }>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode error code
     * @return {@link BaseResponse }
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code    code
     * @param message message
     * @return {@link BaseResponse }<{@link ? }>
     */
    public static BaseResponse<?> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode error code
     * @param message   message
     * @return {@link BaseResponse }<{@link ? }>
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
