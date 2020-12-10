package linkshorten.controller.http

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import linkshorten.domain.{ShortLinkRequest, ShortLinkResponse}
import linkshorten.service.UrlShortenService

import javax.inject.Inject

/**
 * Created by LamNguyen on 08/12/2020
 */
class UrlShortenerController @Inject()(service: UrlShortenService) extends Controller{
  post("/shorten") { request: ShortLinkRequest =>
    logger.info(s"GET Short link for ${request.url}")
    ShortLinkResponse(s"${service.shortenUrl(request.url)}")
  }

  get("/a/:code") { request: Request =>
  {
    val destUrl = service.redirectLink(request.params("code"))
    logger.info(
      s"Redirect from short link ${request.uri} , Dest URL: $destUrl"
    )
    response.movedPermanently.location(destUrl)
  }
  }
}
