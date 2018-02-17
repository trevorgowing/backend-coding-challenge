package com.trevorgowing.expenselist.expense;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Test;

public class ExpenseDTOTests extends AbstractTests {

  @Test
  public void testCreate_shouldConstructExpenseDTOWithCorrectState() {
    Long expenseId = 1L;
    Instant date = Instant.EPOCH;
    BigDecimal amount = new BigDecimal("100.0");
    BigDecimal vat = new BigDecimal("20.0");
    String reason = "reason";
    Long userId = 2L;

    ExpenseDTO expected = ExpenseDTO.builder()
        .id(expenseId)
        .date(date)
        .amount(amount)
        .vat(vat)
        .reason(reason)
        .userId(userId)
        .build();

    ExpenseDTO actual = ExpenseDTO.create(expenseId, date, amount, vat, reason, userId);

    assertThat(actual, is(equalTo(expected)));
  }
}