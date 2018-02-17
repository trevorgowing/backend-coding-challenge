package com.trevorgowing.expenselist.expense;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExpenseRetriever {

  private final ExpenseRepository expenseRepository;

  Expense findById(Long expenseId) {
    return Optional.ofNullable(expenseRepository.findOne(expenseId))
        .orElseThrow(() -> ExpenseNotFoundException.causedBy(
            "No expense found for id: " + expenseId));
  }

  ExpenseDTO findExpenseDTOById(Long expenseId) {
    return Optional.ofNullable(expenseRepository.findExpenseDTOById(expenseId))
        .orElseThrow(() -> ExpenseNotFoundException.causedBy(
            "No expense found for id: " + expenseId));
  }

  List<ExpenseDTO> findExpenseDTOs() {
    return expenseRepository.findExpenseDTOS();
  }
}
