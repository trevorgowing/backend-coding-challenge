package com.trevorgowing.expenselist.common;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

  private int status;
  private String error;
  private String message;

  public static ExceptionResponse create(HttpStatus status, String message) {
    return new ExceptionResponse(status.value(), status.getReasonPhrase(), message);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    ExceptionResponse exceptionResponseToCompareTo = (ExceptionResponse) objectToCompareTo;
    return getStatus() == exceptionResponseToCompareTo.getStatus() &&
        Objects.equals(getError(), exceptionResponseToCompareTo.getError()) &&
        Objects.equals(getMessage(), exceptionResponseToCompareTo.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStatus(), getError(), getMessage());
  }
}
