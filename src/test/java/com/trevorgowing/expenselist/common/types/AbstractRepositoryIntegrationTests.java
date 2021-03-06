package com.trevorgowing.expenselist.common.types;

import org.junit.experimental.categories.Category;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DataJpaTest
@Category(RepositoryIntegrationTests.class)
public abstract class AbstractRepositoryIntegrationTests extends AbstractSpringTests {

  @PersistenceContext
  protected EntityManager entityManager;
}
