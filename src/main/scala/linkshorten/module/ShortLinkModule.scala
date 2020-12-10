package linkshorten.module

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import linkshorten.repository.{DbConnectedShortLinkRepository, OnMemoryShortLinkRepository, ShortLinkRepository}
import linkshorten.service.{UrlShortenService, UrlShortenServiceImpl}
import linkshorten.util.ZConfig

import java.sql.{Connection, DriverManager, SQLException}

/**
 * Created by LamNguyen on 08/12/2020
 */
object ShortLinkModule extends TwitterModule {
  override def configure(): Unit = {
    bind[UrlShortenService].to[UrlShortenServiceImpl]
  }

  @Singleton
  @Provides
  def providesShortLinkRepository(): ShortLinkRepository[String, String] = {
    if (ZConfig.getBoolean("server.service.useDB", false)) {
      val host = ZConfig.getString("server.service.database.host")
      val port = ZConfig.getInt("server.service.database.port")
      val user = ZConfig.getString("server.service.database.user")
      val password = ZConfig.getString("server.service.database.password")
      val db_name = ZConfig.getString("server.service.database.dbname")
        try{
      val conn = DriverManager.getConnection(s"jdbc:mysql://$host:$port/$db_name?user=$user&password=$password")
      new DbConnectedShortLinkRepository[String, String](conn)
        }
      catch {
        case sql :SQLException => {
          logger.error("SQLException: {}" + sql.getMessage)
          logger.error("SQLState: : {}" + sql.getSQLState)
          logger.error("VendorError: {}" + sql.getErrorCode)
          null
        }
      }
    } else {
      new OnMemoryShortLinkRepository[String, String]()
    }
  }
}
