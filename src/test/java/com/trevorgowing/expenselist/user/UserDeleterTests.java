package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.types.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class UserDeleterTests extends AbstractTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserDeleter userDeleter;

  @Test(expected = UserNotFoundException.class)
  public void testDeleteUserWithNoMatchingUser_shouldDelegateToUserRepositoryAndThrowUserNotFoundException() {
    // Set up expectations
    Long userId = 1L;

    doThrow(new EmptyResultDataAccessException(1)).when(userRepository).delete(userId);

    // Exercise SUT
    userDeleter.deleteUser(userId);
  }

  @Test
  public void testDeleteUserWithMatchingUser_shouldDelegateToUserRepositoryToDeleteUser() {
    // Set up expectations
    Long userId = 1L;

    doNothing().when(userRepository).delete(userId);

    // Exercise SUT
    userDeleter.deleteUser(userId);
  }
}
