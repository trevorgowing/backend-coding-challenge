package com.trevorgowing.expenselist.expense;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import com.trevorgowing.expenselist.user.User;
import com.trevorgowing.expenselist.user.UserRetriever;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ExpenseFactoryTests extends AbstractTests {

  @Mock
  private UserRetriever userRetriever;
  @Mock
  private ExpenseRepository expenseRepository;

  @InjectMocks
  private ExpenseFactory expenseFactory;

  @Test
  public void testCreateExpense_shouldConstructExpenseAndDelegateToExpenseRepositoryToSave() {
    Long expenseId = 1L;
    Instant date = Instant.EPOCH;
    BigDecimal amount = new BigDecimal("100.0");
    String reason = "reason";
    Long userId = 2L;

    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .date(date)
        .amount(amount)
        .reason(reason)
        .userId(userId)
        .build();

    User user = User.identifiedUser(userId);
    Expense expectedUnidentifiedExpense = Expense.calculateVatAndCreate(date, amount, reason, user);
    Expense expectedIdentifiedExpense = Expense.calculateVatAndCreate(
        expenseId, date, amount, reason, user);

    when(userRetriever.findUser(userId)).thenReturn(user);
    when(expenseRepository.save(expectedUnidentifiedExpense))
        .thenReturn(expectedIdentifiedExpense);

    Expense actual = expenseFactory.createExpense(expenseDTO);

    assertThat(actual, is(expectedIdentifiedExpense));
  }
}