package org.example.exceptions;

public class APIException extends Exception {
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
