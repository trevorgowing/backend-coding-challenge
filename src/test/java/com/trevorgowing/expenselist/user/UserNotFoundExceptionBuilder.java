package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.builders.DomainObjectBuilder;

import static com.trevorgowing.expenselist.user.UserNotFoundException.identifiedUserNotFoundException;

final class UserNotFoundExceptionBuilder implements DomainObjectBuilder<UserNotFoundException> {

  private long userId;

  static UserNotFoundExceptionBuilder aUserNotFoundException() {
    return new UserNotFoundExceptionBuilder();
  }

  public UserNotFoundException build() {
    return identifiedUserNotFoundException(userId);
  }

  UserNotFoundExceptionBuilder id(long userId) {
    this.userId = userId;
    return this;
  }
}
