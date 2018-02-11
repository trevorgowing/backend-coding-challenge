package com.trevorgowing.expenselist.common.builders;

import java.io.Serializable;

public interface DomainObjectBuilder<DO extends Serializable> {

  DO build();
}
