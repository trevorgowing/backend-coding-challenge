package com.trevorgowing.expenselist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;

@Service
@RequiredArgsConstructor
class UserDeleter {

  private final UserRepository userRepository;

  void deleteUser(long userId) {
    try {
      userRepository.delete(userId);
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw identifiedUserNotFoundException(userId);
    }
  }
}
