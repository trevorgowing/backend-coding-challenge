package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.builders.DomainObjectBuilder;

import static com.trevorgowing.expenselist.user.IdentifiedUserDTO.identifiedUserDTO;

public final class IdentifiedUserDTOBuilder extends AbstractUserDTOBuilder<IdentifiedUserDTOBuilder>
    implements DomainObjectBuilder<IdentifiedUserDTO> {

  private long id;

  public static IdentifiedUserDTOBuilder anIdentifiedUserDTO() {
    return new IdentifiedUserDTOBuilder();
  }

  @Override
  public IdentifiedUserDTO build() {
    return identifiedUserDTO(id, email, password, firstName, lastName);
  }

  public IdentifiedUserDTOBuilder id(long id) {
    this.id = id;
    return this;
  }
}
