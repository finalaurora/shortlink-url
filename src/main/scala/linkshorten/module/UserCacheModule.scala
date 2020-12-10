package linkshorten.module

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import linkshorten.domain.{UserID, UserInfo}
import linkshorten.repository.{CacheRepository, OnMemoryCacheRepository}
import linkshorten.service.{UserCacheService, UserCacheServiceImpl}

import javax.inject.Singleton

/**
  * Created by SangDang on 9/16/16.
  */
object UserCacheModule extends TwitterModule {
  override def configure: Unit = {
    bind[UserCacheService].to[UserCacheServiceImpl]
  }

  @Singleton
  @Provides
  def providesUserCacheRepository(): CacheRepository[UserID, UserInfo] = {
    new OnMemoryCacheRepository[UserID, UserInfo]()
  }
}
