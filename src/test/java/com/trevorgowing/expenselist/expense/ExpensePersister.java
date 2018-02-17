package com.trevorgowing.expenselist.expense;

import com.trevorgowing.expenselist.common.persisters.AbstractEntityPersister;
import com.trevorgowing.expenselist.common.persisters.DefaultEntityPersister;
import com.trevorgowing.expenselist.user.User;
import javax.persistence.EntityManager;

class ExpensePersister extends AbstractEntityPersister<Expense> {

  @Override
  public Expense deepPersist(Expense expense, EntityManager entityManager) {
    if (expense.getUser() != null) {
      new DefaultEntityPersister<User>().deepPersist(expense.getUser(), entityManager);
    }
    entityManager.persist(expense);
    return expense;
  }
}
