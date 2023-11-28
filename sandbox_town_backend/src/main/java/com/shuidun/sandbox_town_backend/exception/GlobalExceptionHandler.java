package com.shuidun.sandbox_town_backend.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.shuidun.sandbox_town_backend.bean.RestResponseVo;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Nullable
    private static String currentUser() {
        if (!StpUtil.isLogin()) {
            return null;
        } else {
            return StpUtil.getLoginIdAsString();
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(Exception e) {
        log.error("User: {} Stack is :\n", currentUser(), e);
        return new RestResponseVo<>(StatusCodeEnum.SERVER_ERROR);
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(NotLoginException e) {
        log.error("e: \"{}\". User: {}\n", e.getMessage(), currentUser());
        return new RestResponseVo<>(StatusCodeEnum.NO_PERMISSION);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(BusinessException e) {
        log.error("e: \"{}\". User: {}\n", e.getMessage(), currentUser());
        return new RestResponseVo<>(e.getStatusCode());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("e: \"{}\". User: {}\n", e.getMessage(), currentUser());
        return new RestResponseVo<>(StatusCodeEnum.REQUEST_METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(ConstraintViolationException e) {
        log.error("e: \"{}\". User: {}\n", e.getMessage(), currentUser());
        return new RestResponseVo<>(StatusCodeEnum.PARAMETER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public RestResponseVo<?> exceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("e: \"{}\". User: {}\n", e.getMessage(), currentUser());
        return new RestResponseVo<>(StatusCodeEnum.PARAMETER_ERROR, "参数类型转化失败");
    }

}