package com.trevorgowing.expenselist.expense;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;

public class ExpenseNotFoundExceptionTests extends AbstractTests {

  @Test
  public void testCausedByWithMessage_shouldConstructWithMessage() {
    String message = "message";

    ExpenseNotFoundException actual = ExpenseNotFoundException.causedBy(message);

    assertThat(actual.getMessage(), is(message));
  }

  @Test
  public void testCausedByWithMessageAndCause_shouldConstructWithMessageAndCause() {
    String message = "message";
    Throwable cause = new Throwable();

    ExpenseNotFoundException actual = ExpenseNotFoundException.causedBy(message, cause);

    assertThat(actual.getMessage(), is(message));
    assertThat(actual.getCause(), is(cause));
  }
}