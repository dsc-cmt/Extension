package io.github.cmt.extension.admin.config;

import javax.servlet.http.HttpServletRequest;

import io.github.cmt.extension.admin.model.BusinessException;
import io.github.cmt.extension.admin.model.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常拦截
 *
 * @author tuzhenxian
 * @date 19-10-11
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleValidationException(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.error("", e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage()).append(";");
        }
        return Result.fail(sb.toString());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(HttpServletRequest req, BusinessException e) {
        log.error("", e);
        return Result.fail(e.getMessage());
    }
}
