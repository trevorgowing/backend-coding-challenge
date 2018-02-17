package com.trevorgowing.expenselist.expense;

import com.trevorgowing.expenselist.common.builders.AbstractEntityBuilder;
import com.trevorgowing.expenselist.common.persisters.AbstractEntityPersister;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;

class ExpenseBuilder extends AbstractEntityBuilder<Expense> {

  private Long id;
  private Instant date;
  private BigDecimal amount;
  private BigDecimal vat;
  private String reason;
  private User user;

  static ExpenseBuilder anExpense() {
    return new ExpenseBuilder();
  }

  @Override
  public AbstractEntityPersister<Expense> getPersister() {
    return new ExpensePersister();
  }

  @Override
  public Expense build() {
    return Expense.identifiedExpense(id, date, amount, vat, reason, user);
  }

  ExpenseBuilder id(Long id) {
    this.id = id;
    return this;
  }

  ExpenseBuilder date(Instant date) {
    this.date = date;
    return this;
  }

  ExpenseBuilder amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  ExpenseBuilder vat(BigDecimal vat) {
    this.vat = vat;
    return this;
  }

  ExpenseBuilder reason(String reason) {
    this.reason = reason;
    return this;
  }

  ExpenseBuilder user(User user) {
    this.user = user;
    return this;
  }
}
