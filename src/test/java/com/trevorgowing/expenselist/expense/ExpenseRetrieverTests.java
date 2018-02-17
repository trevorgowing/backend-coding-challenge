package com.trevorgowing.expenselist.expense;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ExpenseRetrieverTests extends AbstractTests {

  @Mock
  private ExpenseRepository expenseRepository;

  @InjectMocks
  private ExpenseRetriever expenseRetriever;

  @Test(expected = ExpenseNotFoundException.class)
  public void testFindByIdWithNoMatchingExpense_shouldThrowExpenseNotFoundException() {
    Long expenseId = 1L;

    when(expenseRepository.findOne(expenseId)).thenReturn(null);

    expenseRetriever.findById(expenseId);
  }

  @Test
  public void testFindByIdWithMatchingExpense_shouldReturnExpense() {
    Long expenseId = 1L;
    Expense expected = Expense.identifiedExpense(expenseId);

    when(expenseRepository.findOne(expenseId)).thenReturn(expected);

    Expense actual = expenseRetriever.findById(expenseId);

    assertThat(actual, is(expected));
  }

  @Test(expected = ExpenseNotFoundException.class)
  public void testFindExpenseDTOByIdWithNoMatchingExpense_shouldThrowExpenseNotFoundException() {
    Long expenseId = 1L;

    when(expenseRepository.findExpenseDTOById(expenseId)).thenReturn(null);

    expenseRetriever.findById(expenseId);
  }

  @Test
  public void testFindExpenseDTOByIdWithMatchingExpense_shouldReturnExpense() {
    Long expenseId = 1L;
    ExpenseDTO expected = ExpenseDTO.builder().build();

    when(expenseRepository.findExpenseDTOById(expenseId)).thenReturn(expected);

    ExpenseDTO actual = expenseRetriever.findExpenseDTOById(expenseId);

    assertThat(actual, is(expected));
  }

  @Test
  public void findExpenseDTOs_shouldReturnExpenses() {
    ExpenseDTO expenseDTO = ExpenseDTO.builder().build();
    ExpenseDTO anotherExpenseDTO = ExpenseDTO.builder().build();
    List<ExpenseDTO> expenseDTOs = Arrays.asList(expenseDTO, anotherExpenseDTO);

    when(expenseRepository.findExpenseDTOS()).thenReturn(expenseDTOs);

    List<ExpenseDTO> actual = expenseRetriever.findExpenseDTOs();

    assertThat(actual, hasItems(expenseDTO, anotherExpenseDTO));
  }
}