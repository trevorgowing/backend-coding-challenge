package com.trevorgowing.expenselist.expense;

import static com.trevorgowing.expenselist.common.converters.ObjectToJSONConverter.convertToJSON;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.expenselist.common.ExceptionResponse;
import com.trevorgowing.expenselist.common.types.AbstractControllerUnitTests;
import com.trevorgowing.expenselist.user.UserNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

public class ExpenseControllerTests extends AbstractControllerUnitTests {

  @Mock
  private ExpenseFactory expenseFactory;
  @Mock
  private ExpenseDeleter expenseDeleter;
  @Mock
  private ExpenseModifier expenseModifier;
  @Mock
  private ExpenseRetriever expenseRetriever;
  @Mock
  private ExpenseDTOFactory expenseDTOFactory;

  @InjectMocks
  private ExpenseController expenseController;

  @Override
  protected Object getController() {
    return expenseController;
  }

  @Test
  public void testGetExpensesWithNoExpenses_shouldRespondWithStatusOkAndNoExpenses() {
    when(expenseRetriever.findExpenseDTOs()).thenReturn(Collections.emptyList());

    given()
        .accept(JSON)
    .when()
        .get("/app/expenses")
    .then()
        .log().all()
        .statusCode(OK.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(Collections.emptyList()))));

    List<ExpenseDTO> actual = expenseController.getExpenses();

    assertThat(actual, is(empty()));
  }

  @Test
  public void testGetExpensesWithExistingExpenses_shouldRespondWithStatusOkAndExpenses() {
    ExpenseDTO expectedOne = ExpenseDTO.builder().build();
    ExpenseDTO expectedTwo = ExpenseDTO.builder().build();
    List<ExpenseDTO> expected = Arrays.asList(expectedOne, expectedTwo);

    when(expenseRetriever.findExpenseDTOs()).thenReturn(expected);

    given()
        .accept(JSON)
    .when()
        .get("/app/expenses")
    .then()
        .log().all()
        .statusCode(OK.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    List<ExpenseDTO> actual = expenseController.getExpenses();

    assertThat(actual, hasItems(expectedOne, expectedTwo));
  }

  @Test(expected = ExpenseNotFoundException.class)
  public void testGetExpenseByIdWithNoMatchingExpense_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long expenseId = 1L;
    String message = "Expense not found";
    ExceptionResponse expected = ExceptionResponse.builder()
        .status(NOT_FOUND.value())
        .error(NOT_FOUND.getReasonPhrase())
        .message(message)
        .build();

    when(expenseRetriever.findExpenseDTOById(expenseId))
        .thenThrow(ExpenseNotFoundException.causedBy(message));

    given()
        .accept(JSON)
    .when()
        .get("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(NOT_FOUND.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    expenseController.getExpenseById(expenseId);
  }

  @Test
  public void testGetExpenseByIdWithMatchingExpense_shouldResponseWithStatusOkAndExpense() {
    Long expenseId = 1L;
    ExpenseDTO expected = ExpenseDTO.builder().build();

    when(expenseRetriever.findExpenseDTOById(expenseId)).thenReturn(expected);

    given()
        .accept(JSON)
    .when()
        .get("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(OK.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    ExpenseDTO actual = expenseController.getExpenseById(expenseId);

    assertThat(actual, is(expected));
  }

  @Test(expected = UserNotFoundException.class)
  public void testPostExpenseWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    Long userId = 1L;
    String message = "User not found";
    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .userId(userId)
        .build();
    ExceptionResponse expected = ExceptionResponse.builder()
        .status(CONFLICT.value())
        .error(CONFLICT.getReasonPhrase())
        .message(message)
        .build();

    when(expenseFactory.createExpense(expenseDTO))
        .thenThrow(UserNotFoundException.causedBy(message));

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(convertToJSON(expenseDTO))
    .when()
        .post("/app/expenses")
    .then()
        .log().all()
        .statusCode(CONFLICT.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    expenseController.postExpense(expenseDTO);
  }

  @Test
  public void testPostExpenseWithValidExpense_shouldRespondWithStatusCreatedAndExpenseDTO() {
    Long expenseId = 1L;

    ExpenseDTO expenseDTO = ExpenseDTO.builder().build();
    Expense expense = Expense.identifiedExpense(expenseId);
    ExpenseDTO expected = ExpenseDTO.builder().id(expenseId).build();

    when(expenseFactory.createExpense(expenseDTO)).thenReturn(expense);
    when(expenseDTOFactory.createExpenseDTO(expense)).thenReturn(expected);

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(convertToJSON(expenseDTO))
    .when()
        .post("/app/expenses")
    .then()
        .log().all()
        .statusCode(CREATED.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    ExpenseDTO actual = expenseController.postExpense(expenseDTO);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutExpenseWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    Long expenseId = 1L;
    Long userId = 2L;
    String message = "User not found";
    ExpenseDTO expenseDTO = ExpenseDTO.builder()
        .userId(userId)
        .build();
    ExceptionResponse expected = ExceptionResponse.builder()
        .status(CONFLICT.value())
        .error(CONFLICT.getReasonPhrase())
        .message(message)
        .build();

    when(expenseModifier.modifyExpense(expenseId, expenseDTO))
        .thenThrow(UserNotFoundException.causedBy(message));

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(convertToJSON(expenseDTO))
    .when()
        .put("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(CONFLICT.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    expenseController.putExpense(expenseId, expenseDTO);
  }

  @Test(expected = ExpenseNotFoundException.class)
  public void testPutExpenseWithNoMatchingExpense_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long expenseId = 1L;
    ExpenseDTO expenseDTO = ExpenseDTO.builder().id(expenseId).build();
    String message = "Expense not found";
    ExceptionResponse expected = ExceptionResponse.builder()
        .status(NOT_FOUND.value())
        .error(NOT_FOUND.getReasonPhrase())
        .message(message)
        .build();

    when(expenseModifier.modifyExpense(expenseId, expenseDTO))
        .thenThrow(ExpenseNotFoundException.causedBy(message));

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(expenseDTO)
    .when()
        .put("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(NOT_FOUND.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    expenseController.putExpense(expenseId, expenseDTO);
  }

  @Test
  public void testPutExpenseWithValidExpense_shouldRespondWithStatusOkAndExpenseDTO() {
    Long expenseId = 1L;

    ExpenseDTO expenseDTO = ExpenseDTO.builder().id(expenseId).build();
    Expense expense = Expense.identifiedExpense(expenseId);
    ExpenseDTO expected = ExpenseDTO.builder().id(expenseId).build();

    when(expenseModifier.modifyExpense(expenseId, expenseDTO)).thenReturn(expense);
    when(expenseDTOFactory.createExpenseDTO(expense)).thenReturn(expected);

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(convertToJSON(expenseDTO))
    .when()
        .put("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(OK.value())
        .contentType(JSON)
        .body(is(equalTo(convertToJSON(expected))));

    ExpenseDTO actual = expenseController.putExpense(expenseId, expenseDTO);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test(expected = ExpenseNotFoundException.class)
  public void testDeleteExpenseWithNoMatchingExpense_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    Long expenseId = 1L;

    doThrow(ExpenseNotFoundException.causedBy("Expense not found"))
        .when(expenseDeleter).delete(expenseId);

    given()
    .when()
        .delete("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(NOT_FOUND.value());

    expenseController.deleteExpense(expenseId);
  }

  @Test
  public void testDeleteExpenseWithMatchingExpense_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    Long expenseId = 1L;

    doNothing().when(expenseDeleter).delete(expenseId);

    given()
    .when()
        .delete("/app/expenses/" + expenseId)
    .then()
        .log().all()
        .statusCode(NO_CONTENT.value());

    expenseController.deleteExpense(expenseId);
  }

  @Test
  public void testHandleUserNotFoundException_shouldReturnResponseEntityWithStatusConflictAndExceptionResponse() {
    String message = "User not found";
    UserNotFoundException unfe = UserNotFoundException.causedBy(message);

    ResponseEntity<ExceptionResponse> response = expenseController
        .handleUserNotFoundException(unfe);

    assertThat(response.getStatusCode(), is(CONFLICT));
    ExceptionResponse actual = response.getBody();
    assertThat(actual.getStatus(), is(CONFLICT.value()));
    assertThat(actual.getError(), is(CONFLICT.getReasonPhrase()));
    assertThat(actual.getMessage(), is(equalTo(message)));
  }

  @Test
  public void handleExpenseNotFoundException_shouldReturnResponseEntityWithStatusNotFoundAndExceptionResponse() {
    String message = "Expense not found";
    ExpenseNotFoundException enfe = ExpenseNotFoundException.causedBy(message);

    ResponseEntity<ExceptionResponse> response = expenseController
        .handleExpenseNotFoundException(enfe);

    assertThat(response.getStatusCode(), is(NOT_FOUND));
    ExceptionResponse actual = response.getBody();
    assertThat(actual.getStatus(), is(NOT_FOUND.value()));
    assertThat(actual.getError(), is(NOT_FOUND.getReasonPhrase()));
    assertThat(actual.getMessage(), is(equalTo(message)));
  }
}