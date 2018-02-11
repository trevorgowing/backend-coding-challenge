package com.trevorgowing.expenselist.common.persisters;

import com.trevorgowing.expenselist.common.persistence.AbstractPersistable;

import javax.persistence.EntityManager;

public abstract class AbstractEntityPersister<E extends AbstractPersistable> {

  public abstract E deepPersist(E entityToPersist, EntityManager entityManager);
}
