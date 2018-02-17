package com.trevorgowing.expenselist.expense;

import com.trevorgowing.expenselist.user.User;
import com.trevorgowing.expenselist.user.UserRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExpenseFactory {

  private final UserRetriever userRetriever;
  private final ExpenseRepository expenseRepository;

  Expense createExpense(ExpenseDTO expenseDTO) {
    User user = userRetriever.findUser(expenseDTO.getUserId());

    return expenseRepository.save(Expense.calculateVatAndCreate(expenseDTO.getDate(),
        expenseDTO.getAmount(), expenseDTO.getReason(), user));
  }
}
