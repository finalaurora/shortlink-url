package modulex


import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter
import modulex.controller.http
import modulex.controller.http.{HealthController, UrlShortenerController}
import modulex.controller.thrift.CacheController
import modulex.module.{ShortLinkModule, UserCacheModule}
import modulex.util.ZConfig

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
      .add[CacheController]
  }
}
