package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractControllerUnitTests;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.trevorgowing.expenselist.common.converters.ObjectToJSONConverter.convertToJSON;
import static com.trevorgowing.expenselist.user.DuplicateEmailExceptionBuilder.aDuplicateEmailException;
import static com.trevorgowing.expenselist.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.expenselist.user.UnidentifiedUserDTOBuilder.anUnidentifiedUserDTO;
import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;
import static com.trevorgowing.expenselist.user.UserNotFoundExceptionBuilder.aUserNotFoundException;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class UserControllerUnitTests extends AbstractControllerUnitTests {

  @Mock
  private UserFactory userFactory;
  @Mock
  private UserDeleter userDeleter;
  @Mock
  private UserModifier userModifier;
  @Mock
  private UserRetriever userRetriever;
  @Mock
  private UserDTOFactory userDTOFactory;

  @InjectMocks
  private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test
  public void testGetUsersWithNoExistingUsers_shouldRespondWithStatusOKAndReturnNoUsers() throws Exception {
    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOs()).thenReturn(Collections.emptyList());

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(Collections.<IdentifiedUserDTO>emptyList())));

    List<IdentifiedUserDTO> actualUsers = userController.getUsers();

    // Verify behaviour
    assertThat(actualUsers, is(empty()));
  }

  @Test
  public void testGetUsersWithExistingUsers_shouldRespondWithStatusOKAndReturnUsers() throws Exception {
    // Set up fixture
    IdentifiedUserDTO identifiedUserOneDTO = anIdentifiedUserDTO()
        .email("userone@engagetech.com")
        .password("useronepassword")
        .firstName("userone")
        .lastName("lastOne")
        .build();

    IdentifiedUserDTO identifiedUserTwoDTO = anIdentifiedUserDTO()
        .email("usertwo@engagetech.com")
        .password("usertwopassword")
        .firstName("usertwo")
        .lastName("lastTwo")
        .build();

    List<IdentifiedUserDTO> expectedIdentifiedUserDTOs = asList(identifiedUserOneDTO, identifiedUserTwoDTO);

    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOs()).thenReturn(expectedIdentifiedUserDTOs);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/users")
    .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedUserDTOs)));

    List<IdentifiedUserDTO> actualIdentifiedUserDTOs = userController.getUsers();

    // Verify behaviour
    assertThat(actualIdentifiedUserDTOs, is(expectedIdentifiedUserDTOs));
  }

  @Test(expected = UserNotFoundException.class)
  public void testGetUserWithNoMatchingUser_shouldRespondWithStatusNotFound() {
    // Set up expectations
    Long userId = 1L;
    when(userRetriever.findIdentifiedUserDTOById(userId))
        .thenThrow(identifiedUserNotFoundException(userId));

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/users" + "/" + userId)
    .then()
        .log().all()
        .statusCode(HttpStatus.NOT_FOUND.value());

    userController.getUser(userId);
  }

  @Test
  public void testGetUserWithMatchingUser_shouldRespondWithStatusOKAndReturnUser() throws Exception {
    // Set up fixture
    Long userId = 1L;

    IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
        .email("expected@engagetech.com")
        .password("useronepassword")
        .firstName("userone")
        .lastName("lastOne")
        .build();

    // Set up expectations
    when(userRetriever.findIdentifiedUserDTOById(userId))
        .thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
    .when()
        .get("/users" + "/" + userId)
    .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .contentType(ContentType.JSON)
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO = userController.getUser(userId);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = DuplicateEmailException.class)
  public void testPostUserWithDuplicateEmail_shouldRespondWithStatusConflict() throws Exception {
    // Set up fixture
    String email = "duplicate@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    UnidentifiedUserDTO unidentifiedUserDTO = anUnidentifiedUserDTO()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    DuplicateEmailException duplicateEmailException = aDuplicateEmailException()
        .withEmail(email)
        .build();

    // Set up expectations
    when(userFactory.createUser(email, password, firstName, lastName)).thenThrow(duplicateEmailException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedUserDTO))
    .when()
        .post("/users")
    .then()
        .log().all()
        .statusCode(HttpStatus.CONFLICT.value());

    userController.postUser(unidentifiedUserDTO);
  }

  @Test
  public void testPostUserWithUniqueEmail_shouldRespondWithStatusCreatedAndReturnCreatedUser()
      throws Exception {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    UnidentifiedUserDTO unidentifiedUserDTO = anUnidentifiedUserDTO()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    User user = aUser()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userFactory.createUser(email, password, firstName,
        lastName)).thenReturn(user);
    when(userDTOFactory.createIdentifiedUserDTO(user))
        .thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(unidentifiedUserDTO))
    .when()
        .post("/users")
    .then()
        .log().all()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.CREATED.value())
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO = userController.postUser(unidentifiedUserDTO);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutUserWithNoMatchingUser_shouldRespondWithStatusNotFound() throws Exception {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    UserNotFoundException userNotFoundException = aUserNotFoundException()
        .id(userId)
        .build();

    // Set up expectations
    when(userModifier.updateUser(userId, email, password, firstName,
        lastName)).thenThrow(userNotFoundException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
    .when()
        .put("/users" + "/" + userId)
    .then()
        .log().all()
        .statusCode(HttpStatus.NOT_FOUND.value());

    userController.putUser(userId, identifiedUserDTO);
  }

  @Test(expected = DuplicateEmailException.class)
  public void testPutUserWithDuplicateEmail_shouldRespondWithStatusConflict() throws Exception {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    DuplicateEmailException duplicateEmailException = aDuplicateEmailException()
        .withEmail(email)
        .build();

    // Set up expectations
    when(userModifier.updateUser(userId, email, password, firstName,
        lastName)).thenThrow(duplicateEmailException);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
    .when()
    .put("/users" + "/" + userId)
        .then()
        .log().all()
        .statusCode(HttpStatus.CONFLICT.value());

    userController.putUser(userId, identifiedUserDTO);
  }

  @Test
  public void testPutUserWithValidUser_shouldRespondWithStatusOKAndReturnTheUpdatedUser()
      throws Exception {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    IdentifiedUserDTO identifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    User user = aUser()
        .id(userId)
        .email("email")
        .password("password")
        .firstName("first.name")
        .lastName("last.name")
        .build();

    IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userModifier.updateUser(userId, email, password,
        firstName, lastName)).thenReturn(user);
    when(userDTOFactory.createIdentifiedUserDTO(user))
        .thenReturn(expectedIdentifiedUserDTO);

    // Exercise SUT
    given()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(convertToJSON(identifiedUserDTO))
    .when()
        .put("/users" + "/" + userId)
    .then()
        .log().all()
        .contentType(ContentType.JSON)
        .statusCode(HttpStatus.OK.value())
        .body(is(convertToJSON(expectedIdentifiedUserDTO)));

    IdentifiedUserDTO actualIdentifiedUserDTO = userController.putUser(userId, identifiedUserDTO);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testDeleteUserWithNoMatchingUser_shouldRespondWithStatusNotFound() {
    // Set up expectations
    Long userId = 1L;

    doThrow(identifiedUserNotFoundException(userId)).when(userDeleter).deleteUser(userId);

    // Exercise SUT
    given()
    .when()
        .delete("/users" + "/" + userId)
    .then()
        .log().all()
        .statusCode(HttpStatus.NOT_FOUND.value());

    userController.deleteUser(userId);
  }

  @Test
  public void testDeleteUserWithMatchingUser_shouldRespondWithStatusNoContent() {
    // Set up expectations
    Long userId = 1L;

    doNothing().when(userDeleter).deleteUser(userId);

    // Exercise SUT
    given()
    .when()
        .delete("/users" + "/" + userId)
    .then()
        .log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());

    userController.deleteUser(userId);
  }
}
