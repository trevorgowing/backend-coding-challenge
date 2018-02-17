package com.trevorgowing.expenselist.common;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ExceptionResponse> handleUnhandledException(Throwable t) {
    log.error(t.getMessage(), t);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(APPLICATION_JSON)
        .body(ExceptionResponse.create(INTERNAL_SERVER_ERROR, t.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    if (status.is4xxClientError()) {
      log.warn(ex.getMessage(), ex);
    } else if (status.is5xxServerError()) {
      log.error(ex.getMessage(), ex);
    }
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }
}
