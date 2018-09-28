package com.shengxun.common.vo;

import com.shengxun.common.exception.ErrorMessageParser;
import com.shengxun.common.spring.context.BeanFactoryContext;

public class ResultBuilder<T> {

    private ErrorMessageParser errorMessageParser = BeanFactoryContext.getBean("errorMessageParser");

    public static final BaseResult SYSTEM_ERROR_RESULT = new BaseResult("500", "系统异常", "", null);

    public static final BaseResult SUCCESS_RESULT = new BaseResult("200", "成功", "", null);

    private String code;
    private String message;
    private String total;
    private T data;

    public static <T> ResultBuilder<T> get() {
        return new ResultBuilder<T>();
    }

    public ResultBuilder<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public ResultBuilder<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultBuilder<T> setTotal(String total) {
        this.total = total;
        return this;
    }

    public ResultBuilder<T> setData(T data) {
        this.data = data;
        return this;
    }

    public BaseResult<T> build() {
        BaseResult<T> baseResult = new BaseResult<>();
        String code = this.code;
        String message = this.message;
        if (code == null || code.equals("200")) {
            code = "200";
        }
        if(message == null) {
            message = errorMessageParser.getMessage(code);
            if (message == null) {
                String modelName = errorMessageParser.getModelName();
                code = modelName + "." + code;
                message = errorMessageParser.getMessage(code);
            }
            if (message == null) {
                return SYSTEM_ERROR_RESULT;
            }
        }
        baseResult.setCode(code);
        baseResult.setMessage(message);
        baseResult.setTotal(total);
        baseResult.setData(data);
        return baseResult;

    }
}
