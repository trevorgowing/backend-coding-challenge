package com.trevorgowing.expenselist.common.types;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Category(IntegrationTests.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractSpringWebContextTests extends AbstractSpringTests {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void initialiseRestAssuredMockMvc() {
    MockMvc springMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    RestAssuredMockMvc.mockMvc(springMockMvc);
  }
}
