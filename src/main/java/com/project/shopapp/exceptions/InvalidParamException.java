package com.project.shopapp.exceptions;

public class InvalidParamException extends Exception {
    private InvalidParamException(String message) {
        super(message);
    }
}
