package com.trevorgowing.expenselist.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ExpenseDeleter {

  private final ExpenseRepository expenseRepository;

  void delete(Long expenseId) {
    try {
      expenseRepository.delete(expenseId);
    } catch (EmptyResultDataAccessException erdae) {
      throw ExpenseNotFoundException.causedBy(
          "No expense found for id: " + expenseId, erdae);
    }
  }
}
