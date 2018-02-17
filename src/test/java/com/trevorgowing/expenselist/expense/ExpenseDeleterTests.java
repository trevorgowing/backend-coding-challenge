package com.trevorgowing.expenselist.expense;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ExpenseDeleterTests extends AbstractTests {

  @Mock
  private ExpenseRepository expenseRepository;

  @InjectMocks
  private ExpenseDeleter expenseDeleter;

  @Test(expected = ExpenseNotFoundException.class)
  public void testDeleteWithNoMatchingExpense_shouldThrowExpenseNotFoundException() {
    Long expenseId = 1L;

    doThrow(ExpenseNotFoundException.causedBy("Expense not found"))
        .when(expenseRepository).delete(expenseId);

    expenseDeleter.delete(expenseId);
  }

  @Test
  public void testDeleteWithMatchingExpense_shouldDeleteExpense() {
    Long expenseId = 1L;

    expenseDeleter.delete(expenseId);

    verify(expenseRepository).delete(expenseId);
  }
}