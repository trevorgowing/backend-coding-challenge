package com.trevorgowing.expenselist.expense;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long> {

  String QUERY_FIND_EXPENSE_DTOS =
      "SELECT NEW com.trevorgowing.expenselist.expense.ExpenseDTO("
          + "e.id, e.date, e.amount, e.vat, e.reason, e.user.id)"
          + "FROM Expense e";

  @Query(QUERY_FIND_EXPENSE_DTOS)
  List<ExpenseDTO> findExpenseDTOS();

  String QUERY_FIND_EXPENSE_DTO_BY_ID =
      "SELECT NEW com.trevorgowing.expenselist.expense.ExpenseDTO("
          + "e.id, e.date, e.amount, e.vat, e.reason, e.user.id) "
          + "FROM Expense e "
          + "WHERE e.id = ?1";

  @Query(QUERY_FIND_EXPENSE_DTO_BY_ID)
  ExpenseDTO findExpenseDTOById(Long id);
}
