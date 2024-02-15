package com.dnd.namuiwiki.common.exception;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class ApplicationErrorException extends RuntimeException {
    private final ApplicationErrorType errorType;
    private String customMessage;
    private MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

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

    public ApplicationErrorException(ApplicationErrorType errorType, MultiValueMap<String, String> headers) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.headers = headers;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, String customMessage, MultiValueMap<String, String> headers) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.customMessage = customMessage;
        this.headers = headers;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, Throwable t, MultiValueMap<String, String> headers) {
        super(t);
        this.errorType = errorType;
        this.headers = headers;
    }

    public ApplicationErrorException(ApplicationErrorType errorType, Throwable t, String customMessage, MultiValueMap<String, String> headers) {
        super(t);
        this.errorType = errorType;
        this.customMessage = customMessage;
        this.headers = headers;
    }

}
