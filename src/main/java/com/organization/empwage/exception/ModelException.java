package com.organization.empwage.exception;

public class ModelException extends RuntimeException{
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException() {
    }
}
