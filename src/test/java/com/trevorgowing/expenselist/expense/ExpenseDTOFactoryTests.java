package com.trevorgowing.expenselist.expense;

import static com.trevorgowing.expenselist.expense.ExpenseDTOMatcher.hasSameStateAsExpenseDTO;
import static org.junit.Assert.assertThat;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ExpenseDTOFactoryTests extends AbstractTests {

  @InjectMocks
  private ExpenseDTOFactory expenseDTOFactory;

  @Test
  public void testCreateExpenseDTO_shouldConstructDTOWithCorrectState() {
    Long expenseId = 1L;
    Instant date = Instant.EPOCH;
    BigDecimal total = new BigDecimal("100.0");
    BigDecimal amount = new BigDecimal("80.00");
    BigDecimal vat = new BigDecimal("20.00");
    String reason = "reason";
    Long userId = 2L;

    User user = User.identifiedUser(userId);
    Expense expense = Expense.calculateVatAndCreate(expenseId, date, total, reason, user);

    ExpenseDTO expected = ExpenseDTO.builder()
        .id(expenseId)
        .date(date)
        .amount(amount)
        .vat(vat)
        .reason(reason)
        .userId(userId)
        .build();

    ExpenseDTO actual = expenseDTOFactory.createExpenseDTO(expense);

    assertThat(actual, hasSameStateAsExpenseDTO(expected));
  }
}