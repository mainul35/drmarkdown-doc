package com.drmarkdown.doc.exceptions;

public class InvalidPayloadException extends Throwable {
    public InvalidPayloadException(String invalid_payload) {
        super(invalid_payload);
    }
}
