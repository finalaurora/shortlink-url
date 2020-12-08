package modulex.service

import modulex.repository.ShortLinkRepository

import javax.inject.Inject

/**
 * Created by LamNguyen on 08/12/2020
 */
trait UrlShortenService {
  def redirectLink(shortenCode: String): String

  def shortenUrl(url: String): String
}

class UrlShortenServiceImpl @Inject()(shortLinkRepo: ShortLinkRepository[String, String]) extends UrlShortenService {
  override def redirectLink(shortenCode: String) = {
    shortLinkRepo.get(shortenCode)
  }

  override def shortenUrl(url: String): String = {
    val code = sha256Hash(url).substring(0, 10)
    shortLinkRepo.addNew(code, url)
    s"localhost:8080/a/$code"
  }

  private def sha256Hash(text: String): String =
    String.format(
      "%064x",
      new java.math.BigInteger(
        1,
        java.security.MessageDigest
          .getInstance("SHA-256")
          .digest(text.getBytes("UTF-8"))
      )
    )
}

