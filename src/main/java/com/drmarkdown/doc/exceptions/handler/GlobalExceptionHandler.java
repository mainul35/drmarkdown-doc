package com.drmarkdown.doc.exceptions.handler;

import com.drmarkdown.doc.exceptions.InvalidPayloadException;
import com.drmarkdown.doc.exceptions.MissingAuthorizationException;
import com.drmarkdown.doc.exceptions.UserNotAllowedException;
import com.drmarkdown.doc.exceptions.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_CREDENTIALS = "bad_credentials";
    private static final String INVALID_PAYLOAD = "invalid_payload";
    private static final String USER_NOT_ALLOWED = "user_not_allowed";
    private static final String MISSING_AUTHORIZATION_HEADER = "missing_authorization_header";

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsExceptions(final BadCredentialsException e, final WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorIdentifier(BAD_CREDENTIALS);

        log.error(String.valueOf(errorResponse), e);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPayloadException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsExceptions(final InvalidPayloadException e, final WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorIdentifier(INVALID_PAYLOAD);

        log.error(String.valueOf(errorResponse), e);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsExceptions(final UserNotAllowedException e, final WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorIdentifier(USER_NOT_ALLOWED);

        log.error(String.valueOf(errorResponse), e);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsExceptions(final MissingAuthorizationException e, final WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorIdentifier(MISSING_AUTHORIZATION_HEADER);

        log.error(String.valueOf(errorResponse), e);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
