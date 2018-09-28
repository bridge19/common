package com.shengxun.common.exception;

/**
 * 业务异常
 * @author E0446
 *
 */
public class BizException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1060388536264830994L;

    private String code;

    public BizException(String code) {
        super();
        this.code = code;
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
