package com.n26.exception;

import org.springframework.http.HttpStatus;

public class ApiNoContentRequestException extends ApiServerException {
    private static final int STATUS = HttpStatus.NO_CONTENT.value();

    public ApiNoContentRequestException(String message, String transactionId, Throwable cause) {
        super(STATUS, message, transactionId, cause);
    }

    public ApiNoContentRequestException(String message, String transactionId) {
        super(STATUS, message, transactionId);
    }

    public ApiNoContentRequestException(String message, Throwable cause) {
        super(STATUS, message, cause);
    }

    public ApiNoContentRequestException(String message) {
        super(STATUS, message);
    }

}