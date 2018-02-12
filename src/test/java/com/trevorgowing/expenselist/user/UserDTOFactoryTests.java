package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;

import static com.trevorgowing.expenselist.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class UserDTOFactoryTests extends AbstractTests {

  private final UserDTOFactory userDTOFactory = new UserDTOFactory();

  @Test
  public void testCreateIdentifiedUserDTO_shouldCreateIdentifiedUserDTOFromGivenUser() {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";

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

    // Exercise SUT
    IdentifiedUserDTO actualIdentifiedUserDTO = userDTOFactory.createIdentifiedUserDTO(user);

    // Verify behaviour and state
    assertThat(actualIdentifiedUserDTO, samePropertyValuesAs(expectedIdentifiedUserDTO));
  }
}