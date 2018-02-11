package com.trevorgowing.expenselist.common.types;

import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@Category(ServiceUnitTests.class)
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTests {

  @Rule
  public TestRule watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println(description.getDisplayName());
    }
  };
}
