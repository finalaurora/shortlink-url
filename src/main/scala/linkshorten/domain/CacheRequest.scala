package linkshorten.domain

import com.twitter.finatra.http.annotations.QueryParam

/**
  * Created by SangDang on 9/16/16.
  */
case class PutCacheRequest(userID: UserID,userInfo: UserInfo) {
}
case class GetCacheRequest( @QueryParam userID: String) {
}
