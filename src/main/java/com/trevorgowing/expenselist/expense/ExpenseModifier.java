package com.trevorgowing.expenselist.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExpenseModifier {

  private final ExpenseRetriever expenseRetriever;
  private final ExpenseRepository expenseRepository;

  Expense modifyExpense(Long expenseId, ExpenseDTO expenseDTO) {
    Expense expense = expenseRetriever.findById(expenseId);

    if (expenseDTO.getVat() == null) {
      expense.setVat(Expense.calculateVat(expenseDTO.getAmount()));
      expense.setAmount(expenseDTO.getAmount().subtract(expense.getVat()));
    } else {
      expense.setVat(expenseDTO.getVat());
      expense.setAmount(expenseDTO.getAmount());
    }

    expense.setDate(expenseDTO.getDate());
    expense.setReason(expenseDTO.getReason());

    return expenseRepository.save(expense);
  }
}
