package com.trevorgowing.expenselist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static com.trevorgowing.expenselist.user.DuplicateEmailException.causedDuplicateEmailException;

@Service
@RequiredArgsConstructor
class UserModifier {

  private final UserRetriever userRetriever;
  private final UserRepository userRepository;

  User updateUser(long userId, String email, String password, String firstName, String lastName) {
    User userToUpdate = userRetriever.findUser(userId);

    userToUpdate.setEmail(email);
    userToUpdate.setPassword(password);
    userToUpdate.setFirstName(firstName);
    userToUpdate.setLastName(lastName);

    try {
      return userRepository.save(userToUpdate);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw causedDuplicateEmailException(email, dataIntegrityViolationException);
    }
  }
}
