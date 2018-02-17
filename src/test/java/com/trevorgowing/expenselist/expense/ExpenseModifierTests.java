package com.trevorgowing.expenselist.expense;

import static com.trevorgowing.expenselist.expense.ExpenseMatcher.hasSameStateAsExpense;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ExpenseModifierTests extends AbstractTests {

  @Mock
  private ExpenseRetriever expenseRetriever;
  @Mock
  private ExpenseRepository expenseRepository;

  @InjectMocks
  private ExpenseModifier expenseModifier;

  @Test
  public void testModifyExpenseWithNoVat_shouldCalculateVatAndModifyExpense() {
    Long expenseId = 1L;
    Instant date = Instant.EPOCH;
    BigDecimal total = new BigDecimal("100.0");
    BigDecimal amount = new BigDecimal("80.0");
    BigDecimal vat = new BigDecimal("20.0");
    String reason = "reason";
    Long userId = 2L;

    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .id(expenseId)
        .date(date)
        .amount(amount)
        .vat(vat)
        .reason(reason)
        .userId(userId)
        .build();

    User user = User.identifiedUser(userId);
    Expense originalExpense = Expense.calculateVatAndCreate(
        expenseId, Instant.MIN, new BigDecimal("50.0"), "string", user);
    Expense modifiedExpense = Expense.calculateVatAndCreate(expenseId, date, total, reason, user);

    when(expenseRetriever.findById(expenseId)).thenReturn(originalExpense);
    when(expenseRepository.save(modifiedExpense)).thenReturn(modifiedExpense);

    Expense actual = expenseModifier.modifyExpense(expenseId, expenseDTO);

    assertThat(actual, hasSameStateAsExpense(modifiedExpense));
  }

  @Test
  public void testModifyExpenseWithNoVat_shouldModifyExpense() {
    Long expenseId = 1L;
    Instant date = Instant.EPOCH;
    BigDecimal total = new BigDecimal("100.0");
    String reason = "reason";
    Long userId = 2L;

    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .id(expenseId)
        .date(date)
        .amount(total)
        .reason(reason)
        .userId(userId)
        .build();

    User user = User.identifiedUser(userId);
    Expense originalExpense = Expense.calculateVatAndCreate(
        expenseId, Instant.MIN, new BigDecimal("50.0"), "string", user);
    Expense modifiedExpense = Expense.calculateVatAndCreate(expenseId, date, total, reason, user);

    when(expenseRetriever.findById(expenseId)).thenReturn(originalExpense);
    when(expenseRepository.save(modifiedExpense)).thenReturn(modifiedExpense);

    Expense actual = expenseModifier.modifyExpense(expenseId, expenseDTO);

    assertThat(actual, hasSameStateAsExpense(modifiedExpense));
  }
}