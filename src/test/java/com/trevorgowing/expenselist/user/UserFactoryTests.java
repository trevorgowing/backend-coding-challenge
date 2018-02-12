package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class UserFactoryTests extends AbstractTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserFactory userFactory;

  @Test(expected = DuplicateEmailException.class)
  public void testCreateUserWithDuplicateEmail_shouldDelegateToUserRepositoryAndThrowDuplicateEmailException() {
    // Set up fixture
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    User unidentifiedUser = aUser()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userRepository.save(unidentifiedUser))
        .thenThrow(new DataIntegrityViolationException("Duplicate email address"));

    // Exercise SUT
    userFactory.createUser(email, password, firstName, lastName);
  }

  @Test
  public void testCreateUserWithValidUser_shouldDelegateToUserRepositoryToSaveCreatedUserAndReturnManagedUser() {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    User unidentifiedUser = aUser()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    User expectedUser = aUser()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userRepository.save(argThat(samePropertyValuesAs(unidentifiedUser))))
        .thenReturn(expectedUser);

    // Exercise SUT
    User actualUser = userFactory.createUser(email, password, firstName, lastName);

    // Verify behaviour
    assertThat(actualUser, is(expectedUser));
  }
}
