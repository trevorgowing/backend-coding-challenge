package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractRepositoryIntegrationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.trevorgowing.expenselist.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class UserRepositoryIntegrationTests extends AbstractRepositoryIntegrationTests {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindIdentifiedUserDTOsWithNoExistingUsers_shouldReturnNoIdentifiedUserDTOs() {
    // Exercise SUT
    List<IdentifiedUserDTO> actualIdentifiedUserDTOS = userRepository.findIdentifiedUserDTOs();

    // Verify results
    assertThat(actualIdentifiedUserDTOS, is(empty()));
  }

  @Test
  public void testFindIdentifiedUserDTOsWithExistingUsers_shouldReturnIdentifiedUserDTOs() {
    // Set up fixture
    String email = "one@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    User userOne = aUser()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .buildAndPersist(entityManager);

    IdentifiedUserDTO identifiedUserOneDTO = anIdentifiedUserDTO()
        .id(userOne.getId())
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    String emailTwo = "two@engagetech.com";
    String passwordTwo = "passwordTwo";
    String firstNameTwo = "firstTwo";
    String lastNameTwo = "lastTwo";
    
    User userTwo = aUser()
        .email(emailTwo)
        .password(passwordTwo)
        .firstName(firstNameTwo)
        .lastName(lastNameTwo)
        .buildAndPersist(entityManager);

    IdentifiedUserDTO identifiedUserTwoDTO = anIdentifiedUserDTO()
        .id(userTwo.getId())
        .email(emailTwo)
        .password(passwordTwo)
        .firstName(firstNameTwo)
        .lastName(lastNameTwo)
        .build();

    List<IdentifiedUserDTO> expectedIdentifiedUserDTOS = asList(identifiedUserOneDTO, identifiedUserTwoDTO);

    // Exercise SUT
    List<IdentifiedUserDTO> actualIdentifiedUserDTOS = userRepository.findIdentifiedUserDTOs();

    // Verify results
    assertThat(actualIdentifiedUserDTOS, is(expectedIdentifiedUserDTOS));
  }

  @Test
  public void testFindIdentifiedUserDTOByUserIdWithNoMatchingUser_shouldReturnNull() {
    IdentifiedUserDTO identifiedUserDTO = userRepository.findIdentifiedUserDTOById(1L);
    assertThat(identifiedUserDTO, is(nullValue(IdentifiedUserDTO.class)));
  }

  @Test
  public void testFindUserDTOByIdWithMatchingUser_shouldReturnMatchingUser() {
    // Set up fixture
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

    User expectedUser = aUser()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .buildAndPersist(entityManager);

    String emailTwo = "two@engagetech.com";
    String passwordTwo = "passwordTwo";
    String firstNameTwo = "firstTwo";
    String lastNameTwo = "lastTwo";

    aUser()
        .email(emailTwo)
        .password(passwordTwo)
        .firstName(firstNameTwo)
        .lastName(lastNameTwo)
        .buildAndPersist(entityManager);

    IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
        .id(expectedUser.getId())
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Exercise SUT
    IdentifiedUserDTO actualIdentifiedUserDTO = userRepository.findIdentifiedUserDTOById(expectedUser.getId());

    // Verify results
    assertThat(actualIdentifiedUserDTO, samePropertyValuesAs(expectedIdentifiedUserDTO));
  }

  @Test
  public void testFindByEmailWithUnrecognisedEmail_shouldReturnNull() {
    // Set up fixture
    String unrecognisedEmail = "unrecognised@engagetech.com";

    aUser().email("random@engagetech.com").buildAndPersist(entityManager);

    // Exercise SUT
    User actualUser = userRepository.findByEmail(unrecognisedEmail);

    // Verify results
    assertThat(actualUser, is(nullValue(User.class)));
  }

  @Test
  public void testFindByEmailWithRecognisedEmail_shouldReturnUser() {
    // Set up fixture
    String recognisedEmail = "recognised@engagetech.com";

    User expectedUser = aUser().email(recognisedEmail).buildAndPersist(entityManager);

    // Exclude
    aUser().email("random@engagetech.com").buildAndPersist(entityManager);

    // Exercise SUT
    User actualUser = userRepository.findByEmail(recognisedEmail);

    // Verify results
    assertThat(actualUser, is(expectedUser));
  }
}
