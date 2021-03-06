package edu.hehai.shuili.weather.exception;

import edu.hehai.shuili.weather.api.ApiStatus;
import edu.hehai.shuili.weather.exception.error.RestFieldError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 所有异常类的基类
 */
public class BaseRestException extends RuntimeException {

    private static final long serialVersionUID = 1330458449080010936L;
    private HttpStatus status;
    private int code;
    private List<RestFieldError> error;

    public BaseRestException() {
    }

    public BaseRestException(HttpStatus status, int code, String field, Object rejectedValue) {
        this.status = status;
        this.code = code;
        this.error = Arrays.asList(new RestFieldError(field, rejectedValue, ApiStatus.getApiStatus(code).getMessage()));
    }

    public BaseRestException(HttpStatus status, int code, List<RestFieldError> error) {
        this.status = status;
        this.code = code;
        this.error = error;
    }

    public static List<RestFieldError> toRestFieldErrorList(List<FieldError> errors) {
        List<RestFieldError> fieldErrors = new ArrayList<>(errors.size());
        for (FieldError error : errors) {
            fieldErrors.add(new RestFieldError(error));
        }
        return fieldErrors;
    }

    public List<RestFieldError> getErrors() {
        return error;
    }

    public void setErrors(List<RestFieldError> error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    private String getMessageKey() {
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.lastIndexOf("Exception"));
    }
}