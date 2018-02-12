package com.trevorgowing.expenselist.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor
public class IdentifiedUserDTO extends UnidentifiedUserDTO {

  private static final long serialVersionUID = -84805295469064090L;

  private long id;

  /**
   * Used in constructor queries {@link UserRepository#findIdentifiedUserDTOs()} and
   * {@link UserRepository#findIdentifiedUserDTOById(long)}.
   */
  @SuppressWarnings("unused")
  public IdentifiedUserDTO(long id, String email, String password, String firstName, String lastName) {
    super(email, password, firstName, lastName);
    this.id = id;
  }

  static IdentifiedUserDTO identifiedUserDTO(long id, String email, String password, String firstName,
                                             String lastName) {
    return new IdentifiedUserDTO(id, email, password, firstName, lastName);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo) return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass()) return false;
    if (!super.equals(objectToCompareTo)) return false;
    IdentifiedUserDTO identifiedUserDTOToCompareTo = (IdentifiedUserDTO) objectToCompareTo;
    return id == identifiedUserDTOToCompareTo.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
