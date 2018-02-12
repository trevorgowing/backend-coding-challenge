package com.trevorgowing.expenselist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.trevorgowing.expenselist.user.DuplicateEmailException.causedDuplicateEmailException;
import static com.trevorgowing.expenselist.user.User.unidentifiedUser;

@Service
@RequiredArgsConstructor
class UserFactory {

  private final UserRepository userRepository;

  User createUser(String email, String password, String firstName, String lastName) {
    try {
      return userRepository.save(unidentifiedUser(email, password, firstName, lastName));
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw causedDuplicateEmailException(email, dataIntegrityViolationException);
    }
  }
}
