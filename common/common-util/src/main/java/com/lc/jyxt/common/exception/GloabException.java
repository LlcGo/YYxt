package com.lc.jyxt.common.exception;

import com.lc.jyxt.common.result.Result;
import com.lc.jyxt.common.result.ResultCodeEnum;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Lc
 * @Date 2023/6/21
 * @Description
 */
@ControllerAdvice
public class GloabException {

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result YyghExceptionHandler(YyghException e){
        return Result.Error(e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(RuntimeException e){
        return Result.Error(ResultCodeEnum.SERVICE_ERROR,e.getMessage());
    }

}
