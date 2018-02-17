package com.trevorgowing.expenselist.user;

public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6916075569670660878L;

  static final String REASON = "User not found for id";

  private UserNotFoundException(String message) {
    super(message);
  }

  private UserNotFoundException(long userId) {
    super(REASON + ": " + userId);
  }

  public static UserNotFoundException causedBy(String message) {
    return new UserNotFoundException(message);
  }

  public static UserNotFoundException identifiedUserNotFoundException(long userId) {
    return new UserNotFoundException(userId);
  }
}
