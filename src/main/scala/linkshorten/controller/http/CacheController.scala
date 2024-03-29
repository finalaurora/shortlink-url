package linkshorten.controller.http

import javax.inject.{Inject, Singleton}
import com.twitter.finatra.http.Controller
import linkshorten.domain.{GetCacheRequest, PutCacheRequest, UserID}
import linkshorten.service.UserCacheService
import linkshorten.domain.UserID


/**
  * Created by SangDang on 9/16/16.
  */

@Singleton
class CacheController @Inject()(userCacheService: UserCacheService) extends Controller {
  post("/addUser") { request: PutCacheRequest =>
    userCacheService.addUser(request.userID, request.userInfo)
    response.ok
  }
  get("/getUser") {
    request: GetCacheRequest =>
      for {
        userInfo <- userCacheService.getUser(UserID(request.userID))
      } yield {
        response.ok(userInfo)
      }
  }

}