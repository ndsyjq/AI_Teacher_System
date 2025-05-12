package com.example.ai_teacher_system_back.exception;

import lombok.Getter;

/**
 * 业务异常类
 * 用于封装业务逻辑异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * @param message 错误信息
     */
    public BusinessException(String message) {
        this(400, message);
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误信息
     * @param cause 异常原因
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}