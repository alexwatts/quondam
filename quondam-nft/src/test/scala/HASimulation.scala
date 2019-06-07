import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scalaj.http.{Http, HttpOptions, HttpResponse}

import scala.concurrent.duration._
import scala.sys.process._
import scala.util.Random

class HASimulation extends Simulation {

  val conf = ConfigFactory.load()
  val quondam = conf.getString("urls.quondamUrl")
  val payments = conf.getString("urls.paymentsUrl")

  val numberOfKeys = 10000

  val duration = 10 minutes

  val httpProtocol = http.contentTypeHeader("application/json")

  val shard = Random.alphanumeric.take(20).mkString

  val feeder = Iterator.continually(
    Map(
      "shard" -> shard,
      "key" -> Random.nextInt(numberOfKeys)
    )
  )

  before {
    println("Creating Keys ...")
    for (i <- 1 to numberOfKeys) {
      val response: HttpResponse[String] =
        Http(quondam + "/keys").
          postData(s"""{ "shard":"$shard", "key":"$i" }""".getBytes).
          option(HttpOptions.method("PUT")).
          header("content-type", "application/json").asString
    }
    println("Done!")
  }

  after {
    val response: HttpResponse[String] =
      Http(payments + "/reset").
        option(HttpOptions.method("GET")).
        header("content-type", "application/json").asString
  }

  val scn = scenario("Claim Keys")
    .during(duration) {
      feed(feeder).exec(http("delete_key")
        .delete(quondam + "/keys").body(StringBody("""{ "shard": "${shard}", "key":"${key}" }""")).asJson
        .check(bodyString.saveAs("bodyString"))
        .check(status.is(200))

       )

        .doIf(session => session("bodyString").as[String].contains("\"claimed\":true")) {
          exec(http("Make Payment")
              .put(payments + "/payments")
               .body(StringBody("""{ "idempotenceKey": "${key}", "cardNumber":"${key}", "value":"15.00" }""")).asJson
               .check(status.is(201)))
       }
    }

  val chaosScn = scenario("Chaos").
    during(duration) {
      exec(session => {
        val scriptOutput = Process("/chaos.sh").!!
        println(s"""Chaos invoked: ${scriptOutput} """)
        session
      }).pause(1 minute)
    }

  setUp(
    scn.inject(atOnceUsers(5)).protocols(httpProtocol) ,
    chaosScn.inject(atOnceUsers(1)).protocols(httpProtocol)
  ).assertions(
    global.responseTime.mean.lt(50), // mean resp time < 50 ms
    forAll.failedRequests.percent.lt(0.05) // for each request, < 5% failure
  )

}
