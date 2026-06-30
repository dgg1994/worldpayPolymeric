package com.polymeric.exceptionHandler;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandle extends BaseApiService {

    /**
     * @RequestBody + @Valid 的校验异常 - 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseBase handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldError();
        String msg = error != null ? error.getDefaultMessage() : "参数错误";
        return setResult(400, msg, null);
    }

    /**
     * 普通参数校验异常 - 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseBase handleConstraintViolationException(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().iterator().next().getMessage();
        return setResult(400, msg, null);
    }

    /**
     * 表单参数绑定异常 - 400
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseBase handleBindException(BindException ex) {
        FieldError error = ex.getFieldError();
        String msg = error != null ? error.getDefaultMessage() : "参数绑定错误";
        return setResult(400, msg, null);
    }

    /**
     * 请求方法不支持 - 405
     * 例如：接口是 POST，但用 GET 请求
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseBase handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String msg = String.format("请求方法 %s 不支持，请使用 %s", 
                ex.getMethod(), 
                String.join(", ", ex.getSupportedMethods()));
        return setResult(405, msg, null);
    }

    /**
     * Content-Type 不支持 - 415
     * 例如：接口需要 application/json，但传了 application/x-www-form-urlencoded
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResponseBase handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String supportedTypes = ex.getSupportedMediaTypes().stream()
                .map(MediaType::toString)
                .collect(Collectors.joining(", "));
        
        String msg = String.format("Content-Type %s 不支持，请使用 %s", 
                ex.getContentType(),
                supportedTypes);
        return setResult(415, msg, null);
    }
    /**
     * 请求体格式错误 - 400
     * 例如：JSON 格式错误、缺少必需字段等
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseBase handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String msg = "请求体格式错误，请检查参数格式";
        
        // 可以针对常见错误给出更明确的提示
        String message = ex.getMessage();
        if (message != null && message.contains("Required request body is missing")) {
            msg = "缺少请求体";
        } else if (message != null && message.contains("JSON parse error")) {
            msg = "JSON 格式错误，请检查语法";
        }
        
        return setResult(400, msg, null);
    }

    /**
     * 缺少必需的请求参数 - 400
     * 例如：@RequestParam(required=true) 的参数未传递
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseBase handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String msg = String.format("缺少必需参数: %s (类型: %s)", 
                ex.getParameterName(), 
                ex.getParameterType());
        return setResult(400, msg, null);
    }

    /**
     * 参数类型不匹配 - 400
     * 例如：接口需要 Integer，但传了字符串 "abc"
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseBase handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("参数 %s 类型错误，期望类型: %s，实际值: %s", 
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知",
                ex.getValue());
        return setResult(400, msg, null);
    }

    /**
     * 全局异常兜底 - 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @SysLogAnnotation(module = "全局异常捕获", type = "异常捕获", remark = "异常捕获")
    public ResponseBase exceptionHandler(Exception e, HttpServletRequest request) {
        // 打印异常堆栈，方便排查
        e.printStackTrace();
        return setResultError(Constants.ERROR);
    }
}