package com.trevorgowing.expenselist.expense;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
class ExpenseMatcher extends TypeSafeMatcher<Expense> {

  private final Expense expected;

  static ExpenseMatcher hasSameStateAsExpense(Expense expenseDTO) {
    return new ExpenseMatcher(expenseDTO);
  }

  @Override
  protected boolean matchesSafely(Expense actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getDate(), expected.getDate())
        && Objects.equals(actual.getAmount(), expected.getAmount())
        && Objects.equals(actual.getVat(), expected.getVat())
        && Objects.equals(actual.getReason(), expected.getReason())
        && Objects.equals(actual.getUser(), expected.getUser());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("An Expense with state: ")
        .appendText(" id= ").appendValue(expected.getId())
        .appendText(" date= ").appendValue(expected.getDate())
        .appendText(" amount= ").appendValue(expected.getAmount())
        .appendText(" vat= ").appendValue(expected.getVat())
        .appendText(" reason= ").appendValue(expected.getReason())
        .appendValue(" user= ").appendValue(expected.getUser());
  }
}
