package com.example.orderservice;

import static org.hamcrest.MatcherAssert.assertThat;
import com.example.orderservice.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

  @ServiceConnection
  static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.0");
  @LocalServerPort
  private Integer port;


  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static {
    mySQLContainer.start();
  }

  @Test
  void saveOrder() {
    String saveOrder = """
        {
             "skuCode": "T-shirt",
             "price": 100,
             "quantity": 1
        }
        """;

    InventoryClientStub.stubInventoryCall("T-shirt", 1);

    var responseBodyString = RestAssured.given()
        .contentType("application/json")
        .body(saveOrder)
        .when()
        .post("/api/order")
        .then()
        .log().all()
        .statusCode(201)
        .extract()
        .body().asString();

    assertThat(responseBodyString, Matchers.is("Order created successfully!"));
  }
}
