package examplesimulation

import examplesimulation.util.TestConfig
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class BasicSimulation extends Simulation with TestConfig {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(url)
  http("Main API path")

  val csvFeeder = csv("users.csv").circular


  def getUserAssets() = {
    feed(csvFeeder)
      .exec(http("Sign in and get user assets")
        .post("/api/v1/user/signin")
        .header("Content-Type", "application/json")
        .body(StringBody(
          """
            |{
            | "username": "${email}",
            | "password": "SecretQA!1@pass"
            |}
          """.stripMargin))
        .check(jsonPath("$.access_token").saveAs("token"))
        .check(status is 200))
      .exec(http("GET User Assets")
        .get("/api/v1/user/assets")
        .header("Authorization", "Bearer ${token}")
        .check(status is 200))
  }
  val scn = scenario("Scenario of sign in user")
    .exec(getUserAssets())



  setUp(scn.inject(rampUsersPerSec(1).to(10).during(10)).protocols(httpProtocol))
}
