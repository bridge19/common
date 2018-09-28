package com.shengxun.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shengxun.common.vo.BaseResult;
import com.shengxun.common.vo.ResultBuilder;

/**
 * 全局异常捕获
 * @author E0446
 * 
 */
@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @Autowired private ErrorMessageParser errorMessageParser;
    
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public BaseResult<String> handlerBizException(BizException e) {
        String modelName = errorMessageParser.getModelName();
        String code = e.getCode();
        String message = e.getMessage();
        if(message == null){
            message = errorMessageParser.getMessage(code);
        }
        if(message == null){
            code = modelName+'.'+code;
            message = errorMessageParser.getMessage(code);
        }
        logger.error(message, e);
        if(message == null){
            return ResultBuilder.SYSTEM_ERROR_RESULT;
        }
        return  new BaseResult<String>(code,message,"",null);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public BaseResult<String> handlerOtherException(Exception e) {
        logger.error("System error", e);
        return ResultBuilder.SYSTEM_ERROR_RESULT;
    }
}