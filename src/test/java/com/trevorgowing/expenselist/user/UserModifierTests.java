package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class UserModifierTests extends AbstractTests {

  @Mock
  private UserRetriever userRetriever;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserModifier userModifier;

  @Test(expected = UserNotFoundException.class)
  public void testUpdateUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
    // Set up expectations
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    when(userRetriever.findUser(userId)).thenThrow(identifiedUserNotFoundException(userId));

    // Exercise SUT
    userModifier.updateUser(userId, email, password, firstName, lastName);
  }

  @Test(expected = DuplicateEmailException.class)
  public void testUpdateUserWithDuplicateEmail_shouldDelegateToUserRepositoryAndThrowDuplicateUserException() {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    User userWithDuplicateEmail = aUser()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userRetriever.findUser(userId)).thenReturn(userWithDuplicateEmail);
    when(userRepository.save(userWithDuplicateEmail))
        .thenThrow(new DataIntegrityViolationException("Duplicate email addresss"));

    // Exercise SUT
    userModifier.updateUser(userId, email, password, firstName, lastName);
  }

  @Test
  public void testUpdateUserWithValidUser_shouldDelegateToUserRepositoryAndReturnUpdatedUser() {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    User expectedUser = aUser()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userRetriever.findUser(userId)).thenReturn(expectedUser);
    when(userRepository.save(argThat(samePropertyValuesAs(expectedUser)))).thenReturn(expectedUser);

    // Exercise SUT
    User actualUser = userModifier.updateUser(userId, email, password, firstName, lastName);

    assertThat(actualUser, is(expectedUser));
  }
}