package com.trevorgowing.expenselist.user;

import org.springframework.stereotype.Service;

import static com.trevorgowing.expenselist.user.IdentifiedUserDTO.identifiedUserDTO;

@Service
class UserDTOFactory {

  IdentifiedUserDTO createIdentifiedUserDTO(User user) {
    return identifiedUserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getFirstName(),
        user.getLastName());
  }
}
