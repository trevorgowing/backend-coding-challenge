package com.trevorgowing.expenselist.expense;

import org.springframework.stereotype.Service;

@Service
class ExpenseDTOFactory {

  ExpenseDTO createExpenseDTO(Expense expense) {
    return ExpenseDTO.create(expense.getId(), expense.getDate(), expense.getAmount(),
        expense.getVat(), expense.getReason(), expense.getUser().getId());
  }
}
