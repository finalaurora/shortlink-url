package linkshorten

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter
import linkshorten.controller.http.{CacheController, HealthController, UrlShortenerController}
import linkshorten.controller.http
import linkshorten.controller.thrift
import linkshorten.controller.thrift.CacheController
import linkshorten.module.{ShortLinkModule, UserCacheModule}
import linkshorten.util.ZConfig
import linkshorten.module.UserCacheModule

/**
  * Created by SangDang on 9/8/
  **/
object MainApp extends Server
class Server extends HttpServer with ThriftServer {

  override protected def defaultHttpPort: String = ZConfig.getString("server.http.port",":8080")

  override protected def defaultThriftPort: String = ZConfig.getString("server.thrift.port",":8082")

  override protected def disableAdminHttpServer: Boolean = ZConfig.getBoolean("server.admin.disable",true)

  override val modules = Seq(UserCacheModule,ShortLinkModule)

  override protected def configureHttp(router: HttpRouter): Unit = {
    router.filter[CommonFilters]
      .add[http.CacheController]
      .add[HealthController]
      .add[UrlShortenerController]
  }

  override protected def configureThrift(router: ThriftRouter): Unit = {
    router
      .add[thrift.CacheController]
  }
}
