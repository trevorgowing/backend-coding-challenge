package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static com.trevorgowing.expenselist.user.IdentifiedUserDTOBuilder.anIdentifiedUserDTO;
import static com.trevorgowing.expenselist.user.UserBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class UserRetrieverTests extends AbstractTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserRetriever userRetriever;

  @Test(expected = UserNotFoundException.class)
  public void testFindUserWithNoMatchingUser_shouldThrowUserNotFoundException() {
    // Set up expectations
    Long userId = 1L;
    
    when(userRepository.findOne(userId)).thenReturn(null);

    // Exercise SUT
    userRetriever.findUser(userId);
  }

  @Test
  public void testFindUserWithMatchingUser_shouldReturnUser() {
    // Set up fixture
    Long userId = 1L;
    
    User expectedUser = aUser()
        .id(userId)
        .build();

    // Set up expectations
    when(userRepository.findOne(userId)).thenReturn(expectedUser);

    // Exercise SUT
    User actualUser = userRetriever.findUser(userId);

    // Verify behaviour
    assertThat(actualUser, is(expectedUser));
  }

  @Test
  public void testFindIdentifiedUserDTOs_shouldDelegateToUserRepositoryAndReturnIdentifiedUserDTOs() {
    // Set up fixture
    List<IdentifiedUserDTO> expectedIdentifiedUserDTOs = Collections.emptyList();

    // Set up expectations
    when(userRepository.findIdentifiedUserDTOs()).thenReturn(expectedIdentifiedUserDTOs);

    // Exercise SUT
    List<IdentifiedUserDTO> actualIdentifiedUserDTOs = userRetriever.findIdentifiedUserDTOs();

    // Verify behaviour
    assertThat(actualIdentifiedUserDTOs, is(expectedIdentifiedUserDTOs));
  }

  @Test(expected = UserNotFoundException.class)
  public void testFindIdentifiedUserDTOByIdWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
    // Set up expectations
    Long userId = 1L;
    
    when(userRepository.findIdentifiedUserDTOById(userId)).thenReturn(null);

    // Exercise SUT
    userRetriever.findIdentifiedUserDTOById(userId);
  }

  @Test
  public void testFindIdentifiedUserDTOByIdWithMatchingUser_shouldDelegateToUserRepositoryAndReturnIdentifiedUserDTO() {
    // Set up fixture
    Long userId = 1L;
    String email = "unique@engagetech.com";
    String password = "password";
    String firstName = "first";
    String lastName = "last";
    
    IdentifiedUserDTO expectedIdentifiedUserDTO = anIdentifiedUserDTO()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Set up expectations
    when(userRepository.findIdentifiedUserDTOById(userId)).thenReturn(expectedIdentifiedUserDTO);

    //Exercise SUT
    IdentifiedUserDTO actualIdentifiedUserDTO = userRetriever.findIdentifiedUserDTOById(userId);

    // Verify behaviour
    assertThat(actualIdentifiedUserDTO, is(expectedIdentifiedUserDTO));
  }

}
