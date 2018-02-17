package com.trevorgowing.expenselist.expense;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
class ExpenseDTOMatcher extends TypeSafeMatcher<ExpenseDTO> {

  private final ExpenseDTO expected;

  static ExpenseDTOMatcher hasSameStateAsExpenseDTO(ExpenseDTO expenseDTO) {
    return new ExpenseDTOMatcher(expenseDTO);
  }

  @Override
  protected boolean matchesSafely(ExpenseDTO actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getDate(), expected.getDate())
        && Objects.equals(actual.getAmount(), expected.getAmount())
        && Objects.equals(actual.getVat(), expected.getVat())
        && Objects.equals(actual.getReason(), expected.getReason())
        && Objects.equals(actual.getUserId(), expected.getUserId());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An ExpenseDTO with state: ")
        .appendText(" id= ").appendValue(expected.getId())
        .appendText(" date= ").appendValue(expected.getDate())
        .appendText(" amount= ").appendValue(expected.getAmount())
        .appendText(" vat= ").appendValue(expected.getVat())
        .appendText(" reason= ").appendValue(expected.getReason())
        .appendValue(" userId= ").appendValue(expected.getUserId());
  }
}
