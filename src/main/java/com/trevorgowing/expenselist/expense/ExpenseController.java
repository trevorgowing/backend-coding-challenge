package com.trevorgowing.expenselist.expense;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.trevorgowing.expenselist.common.ExceptionResponse;
import com.trevorgowing.expenselist.user.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/app/expenses")
class ExpenseController {

  private final ExpenseFactory expenseFactory;
  private final ExpenseDeleter expenseDeleter;
  private final ExpenseModifier expenseModifier;
  private final ExpenseRetriever expenseRetriever;
  private final ExpenseDTOFactory expenseDTOFactory;

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  List<ExpenseDTO> getExpenses() {
    return expenseRetriever.findExpenseDTOs();
  }

  @GetMapping(path = "{expenseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  ExpenseDTO getExpenseById(@PathVariable Long expenseId) {
    return expenseRetriever.findExpenseDTOById(expenseId);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(CREATED)
  ExpenseDTO postExpense(@RequestBody ExpenseDTO expenseDTO) {
    Expense expense = expenseFactory.createExpense(expenseDTO);
    return expenseDTOFactory.createExpenseDTO(expense);
  }

  @PutMapping(path = "{expenseId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  ExpenseDTO putExpense(@PathVariable Long expenseId, @RequestBody ExpenseDTO expenseDTO) {
    Expense expense = expenseModifier.modifyExpense(expenseId, expenseDTO);
    return expenseDTOFactory.createExpenseDTO(expense);
  }

  @DeleteMapping(path = "{expenseId}")
  @ResponseStatus(NO_CONTENT)
  void deleteExpense(@PathVariable Long expenseId) {
    expenseDeleter.delete(expenseId);
  }

  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException unfe) {
    log.warn(unfe.getMessage(), unfe);
    return ResponseEntity.status(CONFLICT).contentType(APPLICATION_JSON)
        .body(ExceptionResponse.create(CONFLICT, unfe.getMessage()));
  }

  @ExceptionHandler(ExpenseNotFoundException.class)
  ResponseEntity<ExceptionResponse> handleExpenseNotFoundException(ExpenseNotFoundException enfe) {
    log.warn(enfe.getMessage(), enfe);
    return ResponseEntity.status(NOT_FOUND).contentType(APPLICATION_JSON)
        .body(ExceptionResponse.create(NOT_FOUND, enfe.getMessage()));
  }
}

