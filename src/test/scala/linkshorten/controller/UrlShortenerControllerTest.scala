package linkshorten.controller

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.finatra.thrift.ThriftClient
import com.twitter.inject.server.{EmbeddedTwitterServer, FeatureTest, Ports}
import linkshorten.Server

/**
 * Created by LamNguyen on 09/12/2020
 */
class UrlShortenerControllerTest extends  FeatureTest{
  override protected val server: EmbeddedHttpServer = new EmbeddedHttpServer(twitterServer = new Server) with ThriftClient

  test("UrlShortenerControllerTest#Get link for https://google.com"){
    val resp = server.httpPost(
      path = "/shorten",
      postBody =
        """
          |{
          | "url" : "https://youtube.com"
          |}
          |""".stripMargin
    )
    assert(resp.statusCode==200)
    assert(resp.contentString.prettifier.toString==
      """
        |{
        |  "short_url": "localhost:8080/a/1467931a0e"
        |}
        |""".stripMargin.prettifier.toString)
  }
}
