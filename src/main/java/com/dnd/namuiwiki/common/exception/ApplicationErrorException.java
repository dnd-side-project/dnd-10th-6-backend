package com.dnd.namuiwiki.common.exception;

import lombok.Getter;

@Getter
public class ApplicationErrorException extends RuntimeException {
    private final ApplicationErrorType errorType;
    private String customMessage;

    public ApplicationErrorException(ApplicationErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, String customMessage) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.customMessage = customMessage;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, Throwable t) {
        super(t);
        this.errorType = errorType;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, Throwable t, String customMessage) {
        super(t);
        this.errorType = errorType;
        this.customMessage = customMessage;
    }
}
