package com.george.template.exception;

public class TemplateNotFoundException extends RuntimeException{
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
