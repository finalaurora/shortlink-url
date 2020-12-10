package linkshorten.repository

import com.twitter.util.logging.Logging
import linkshorten.util.ZConfig

import java.sql.{Connection, ResultSet, SQLException}
import javax.inject.Inject
import scala.collection.mutable

/**
 * Created by LamNguyen on 08/12/2020
 */
trait ShortLinkRepository [K,V]{
  def addNew(keyCode: K, originalUrl: V)
  def get(keyCode: K): V
}

class OnMemoryShortLinkRepository[K,V] extends ShortLinkRepository [K,V]{
  val shortLinkCollection = new mutable.HashMap[K, V]()

  override def addNew(keyCode: K, originalUrl: V): Unit = {
    if (!shortLinkCollection.values.exists(_ == originalUrl)) {
      shortLinkCollection.put(keyCode, originalUrl)
    }
  }

  override def get(keyCode: K): V = {
    return shortLinkCollection(keyCode)
  }
}

class DbConnectedShortLinkRepository[K,V] (conn: Connection) extends ShortLinkRepository[K,V] with Logging{

  val dbname = ZConfig.getString("server.service.database.dbname")
  val tbl_name= ZConfig.getString("server.service.database.tbl_name")

  override def addNew(keyCode: K, originalUrl: V): Unit = {

    try {
      val stm = conn.createStatement()
      val sql = s"INSERT INTO $dbname.$tbl_name (`CODE_ID`, `URL`) VALUES ('$keyCode','$originalUrl');"
      stm.executeUpdate(sql)
    }
    catch {
      case sql: SQLException =>
        logger.error("SQLException: {}" + sql.getMessage)
        logger.error("SQLState: : {}" + sql.getSQLState)
        logger.error("VendorError: {}" + sql.getErrorCode)
    }
    }

  override def get(keyCode: K): V = {
    var url = "".asInstanceOf[V]
    try{
    val stm = conn.createStatement()
    val rs: ResultSet = stm.executeQuery(s"SELECT * FROM `$dbname`.`$tbl_name` WHERE `CODE_ID` = '$keyCode';")
      if(rs.next()) {
        url = rs.getObject("URL").asInstanceOf[V]
        val stm2 = conn.createStatement()
        stm2.executeUpdate(
          s"UPDATE $dbname.$tbl_name " +
            s"SET" +
            s"`HIT_COUNT` = `HIT_COUNT` + 1 " +
            s"WHERE `CODE_ID` = '$keyCode';")
      }
      url
    }
    catch {
        case e: SQLException => logger.error("SQL Exception error, {}", e)
        return url
    }
  }
}
