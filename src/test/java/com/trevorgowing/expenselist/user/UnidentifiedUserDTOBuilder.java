package com.trevorgowing.expenselist.user;

import com.trevorgowing.expenselist.common.builders.DomainObjectBuilder;

import static com.trevorgowing.expenselist.user.UnidentifiedUserDTO.unidentifiedUserDTO;

public final class UnidentifiedUserDTOBuilder extends AbstractUserDTOBuilder<UnidentifiedUserDTOBuilder>
    implements DomainObjectBuilder<UnidentifiedUserDTO> {

  public static UnidentifiedUserDTOBuilder anUnidentifiedUserDTO() {
    return new UnidentifiedUserDTOBuilder();
  }

  @Override
  public UnidentifiedUserDTO build() {
    return unidentifiedUserDTO(email, password, firstName, lastName);
  }
}
