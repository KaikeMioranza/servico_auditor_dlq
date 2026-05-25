package com.servico_auditor_dlq.exception;

public class AuditoriaException extends RuntimeException {

    public AuditoriaException(String message) {
        super(message);
    }

    public AuditoriaException(String message, Throwable cause) {
        super(message, cause);
    }
}