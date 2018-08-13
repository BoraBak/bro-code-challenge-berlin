package com.n26.exception;

import org.springframework.http.HttpStatus;

public class ApiUnprocessableEntityRequestException extends ApiServerException {
    private static final int STATUS = HttpStatus.UNPROCESSABLE_ENTITY.value();

    public ApiUnprocessableEntityRequestException(String message, String transactionId, Throwable cause) {
        super(STATUS, message, transactionId, cause);
    }

    public ApiUnprocessableEntityRequestException(String message, String transactionId) {
        super(STATUS, message, transactionId);
    }

    public ApiUnprocessableEntityRequestException(String message, Throwable cause) {
        super(STATUS, message, cause);
    }

    public ApiUnprocessableEntityRequestException(String message) {
        super(STATUS, message);
    }

}