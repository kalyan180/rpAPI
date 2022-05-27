package com.benchmarkuniverse.rp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException{
    private Issue type;
    public UnAuthorizedException() {
    }
    public UnAuthorizedException(String message) {
        super(message);
    }
    public UnAuthorizedException(Issue type, String message) {
        super(message);
        this.type = type;
    }
    public enum Issue {
        UNAUTHORIZED_ACCESS
    }
}
