package com.trevorgowing.expenselist.user;

import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserRetriever {

  private final UserRepository userRepository;

  UserRetriever(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findUser(long userId) {
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
