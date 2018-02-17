package com.trevorgowing.expenselist.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandlerTests extends AbstractTests {

  @InjectMocks
  private GlobalExceptionHandler globalExceptionHandler;

  @Test
  public void testHandleUnhandledException_shouldReturnInternalSeverErrorExceptionResponse() {
    String message = "Something terrible happened";
    Throwable throwable = new Throwable(message);

    ResponseEntity<ExceptionResponse> actual = globalExceptionHandler
        .handleUnhandledException(throwable);

    assertThat(actual.getStatusCode(), is(equalTo(INTERNAL_SERVER_ERROR)));
    ExceptionResponse actualBody = actual.getBody();
    assertThat(actualBody.getStatus(), is(equalTo(INTERNAL_SERVER_ERROR.value())));
    assertThat(actualBody.getError(), is(equalTo(INTERNAL_SERVER_ERROR.getReasonPhrase())));
    assertThat(actualBody.getMessage(), is(equalTo(message)));
  }
}
