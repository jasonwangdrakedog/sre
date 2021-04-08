package com.teg.analysis.exception;

public class BusinessException extends RuntimeException {


    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }


    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
    }

}
