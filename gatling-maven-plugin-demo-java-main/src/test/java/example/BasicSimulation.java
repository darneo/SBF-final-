package example;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class BasicSimulation extends Simulation {

  // Load VU count from system properties (how many virtual users to simulate)
  private static final int vu = Integer.getInteger("vu", 200);

  // Define HTTP configuration
  private static final HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8082")  // Поменяй на свой базовый URL
          .acceptHeader("application/json")
          .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");

  // Define scenario (we send a POST request to create an order)
  private static final ScenarioBuilder scenario = scenario("Create Order Scenario")
          .exec(http("Create Order")
                  .post("/api/orders")  // Путь до API для создания заказа
                  .body(StringBody("{ \"description\": \"Test order\" }")).asJson()  // Тело запроса с данными заказа
                  .check(status().is(200))  // Проверка, что запрос завершился успешно
          );

  // Define assertions to check that no requests failed
  private static final Assertion assertion = global().failedRequests().count().lt(1L);

  // Define injection profile and execute the test
  {
    setUp(scenario.injectOpen(atOnceUsers(vu)))  // Inject virtual users
            .assertions(assertion)  // Ensure no requests fail
            .protocols(httpProtocol);  // Use the defined HTTP protocol
  }
}