package com.trevorgowing.expenselist.user;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

@Service
class UserRetriever {

  private final UserRepository userRepository;

  UserRetriever(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  User findUser(long userId) {
    return ofNullable(userRepository.findOne(userId))
        .orElseThrow(() -> identifiedUserNotFoundException(userId));
  }

  List<IdentifiedUserDTO> findIdentifiedUserDTOs() {
    return userRepository.findIdentifiedUserDTOs();
  }

  IdentifiedUserDTO findIdentifiedUserDTOById(long userId) {
    return ofNullable(userRepository.findIdentifiedUserDTOById(userId))
        .orElseThrow(() -> identifiedUserNotFoundException(userId));
  }
}
