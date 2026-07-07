package com.mukesh.order.exception;

public class KafkaPublishException extends RuntimeException{
    public KafkaPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
