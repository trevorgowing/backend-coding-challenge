package com.trevorgowing.expenselist.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;

public class ExceptionResponseTests extends AbstractTests {

  @Test
  public void testCreate_shouldConstructNewExceptionResponseWithCorrectState() {
    String message = "Something terrible happened";
    ExceptionResponse expected = ExceptionResponse.builder()
        .status(INTERNAL_SERVER_ERROR.value())
        .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(message)
        .build();

    ExceptionResponse actual = ExceptionResponse.create(INTERNAL_SERVER_ERROR, message);

    assertThat(actual, is(equalTo(expected)));
  }
}
