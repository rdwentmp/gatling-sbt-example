package examplesimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}

class WebSocketExample extends Simulation {

  val httpProtocol = http.baseUrl("http://demo.kazzing.com").wsBaseUrl("ws://demos.kaazing.com")

  val scn = scenario("testWebSocketScenario")
        .exec(http("first request").get("/")).pause(2)
        exec(ws("Connect WS").connect("/echo.k1=Y")
        .onConnected(
          exec(ws("Perform auth")
            .sendText("Some auth token")
          .await("20")(ws.checkTextMessage("check name").check(regex(".*auth.*")))
        )))
        .exec(ws("Close conection").close)

    setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
