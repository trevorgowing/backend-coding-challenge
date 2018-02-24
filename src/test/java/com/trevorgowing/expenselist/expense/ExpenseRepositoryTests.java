package com.trevorgowing.expenselist.expense;

import static com.trevorgowing.expenselist.expense.ExpenseBuilder.anExpense;
import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.expenselist.common.types.AbstractRepositoryIntegrationTests;
import com.trevorgowing.expenselist.user.User;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExpenseRepositoryTests extends AbstractRepositoryIntegrationTests {

  @Autowired
  private ExpenseRepository expenseRepository;

  @Test
  public void testFindExpenseDTOS_shouldReturnExpenseDTOs() {
    User user = aUser()
        .email("user@engage.com")
        .build();

    Expense expense = anExpense()
        .date(Instant.parse("2018-02-17T14:30:00Z"))
        .amount(new BigDecimal("80.00"))
        .vat(new BigDecimal("20.00"))
        .reason("reason one")
        .user(user)
        .buildAndPersist(entityManager);

    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .id(expense.getId())
        .date(expense.getDate())
        .amount(expense.getAmount())
        .vat(expense.getVat())
        .reason(expense.getReason())
        .userId(user.getId())
        .build();

    Expense anotherExpense = anExpense()
        .date(Instant.parse("2018-02-24T14:30:00Z"))
        .amount(new BigDecimal("800.00"))
        .vat(new BigDecimal("200.00"))
        .reason("reason two")
        .user(user)
        .buildAndPersist(entityManager);

    ExpenseDTO anotherExpenseDTO = ExpenseDTO.builder()
        .id(anotherExpense.getId())
        .date(anotherExpense.getDate())
        .amount(anotherExpense.getAmount())
        .vat(anotherExpense.getVat())
        .reason(anotherExpense.getReason())
        .userId(user.getId())
        .build();

    List<ExpenseDTO> actualExpenses = expenseRepository.findExpenseDTOS();

    assertThat(actualExpenses, hasItems(expenseDTO, anotherExpenseDTO));
  }

  @Test
  public void findExpenseDTOById() {
    User user = aUser()
        .email("user@engage.com")
        .build();

    Expense expense = anExpense()
        .date(Instant.parse("2018-02-17T14:30:00Z"))
        .amount(new BigDecimal("80.00"))
        .vat(new BigDecimal("20.00"))
        .reason("reason one")
        .user(user)
        .buildAndPersist(entityManager);

    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .id(expense.getId())
        .date(expense.getDate())
        .amount(expense.getAmount())
        .vat(expense.getVat())
        .reason(expense.getReason())
        .userId(user.getId())
        .build();

    anExpense()
        .date(Instant.parse("2018-02-24T14:30:00Z"))
        .amount(new BigDecimal("800.00"))
        .vat(new BigDecimal("200.00"))
        .reason("reason two")
        .user(user)
        .buildAndPersist(entityManager);

    ExpenseDTO actual = expenseRepository.findExpenseDTOById(expense.getId());

    assertThat(actual, is(equalTo(expenseDTO)));
  }
}