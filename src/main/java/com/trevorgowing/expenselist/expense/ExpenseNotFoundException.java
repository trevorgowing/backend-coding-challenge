package com.trevorgowing.expenselist.expense;

import lombok.NoArgsConstructor;

@NoArgsConstructor
class ExpenseNotFoundException extends RuntimeException {

  private ExpenseNotFoundException(String message) {
    super(message);
  }

  private ExpenseNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  static ExpenseNotFoundException causedBy(String message) {
    return new ExpenseNotFoundException(message);
  }

  static ExpenseNotFoundException causedBy(String message, Throwable cause) {
    return new ExpenseNotFoundException(message, cause);
  }
}
