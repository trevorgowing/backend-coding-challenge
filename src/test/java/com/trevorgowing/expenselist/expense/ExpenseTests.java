package com.trevorgowing.expenselist.expense;

import static com.trevorgowing.expenselist.user.User.identifiedUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Test;

public class ExpenseTests extends AbstractTests {

  @Test
  public void testIdentifiedExpense_shouldConstructExpenseWithId() {
    Long expenseId = 1L;

    Expense actual = Expense.identifiedExpense(expenseId);

    assertThat(actual.getId(), is(equalTo(expenseId)));
  }

  @Test
  public void testCalculateVatAndCreate_shouldCalculateVatAndConstructWithCorrectState() {
    Instant date = Instant.EPOCH;
    BigDecimal amount = new BigDecimal("100.0");
    BigDecimal vat = amount.multiply(new BigDecimal("0.2"));
    BigDecimal amountMinusVat = amount.subtract(vat);
    BigDecimal error = new BigDecimal("0.99");
    String reason = "reason";
    Long userId = 2L;
    User user = identifiedUser(userId);

    Expense actual = Expense.calculateVatAndCreate(date, amount, reason, user);

    assertThat(actual.getDate(), is(date));
    assertThat(actual.getAmount(), is(closeTo(amountMinusVat, error)));
    assertThat(actual.getVat(), is(closeTo(vat, error)));
    assertThat(actual.getReason(), is(reason));
    assertThat(actual.getUser(), is(user));
  }

  @Test
  public void testCalculateVat_shouldReturnValueEqualTo20PercentOfAmount() {
    BigDecimal amount = new BigDecimal("100.0");
    BigDecimal expected = new BigDecimal("20.0");
    BigDecimal error = new BigDecimal("0.99");

    BigDecimal actual = Expense.calculateVat(amount);

    assertThat(actual, is(closeTo(expected, error)));
  }
}