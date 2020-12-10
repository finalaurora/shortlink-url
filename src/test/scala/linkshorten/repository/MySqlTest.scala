package linkshorten.repository

import com.twitter.inject.server.FeatureTest
import vn.rever.common.util.Utils
import vn.rever.jdbc.mysql.MysqlEngine

/**
 * Created by LamNguyen on 10/12/2020
 */
object MySqlTest  {
  def main(args: Array[String]): Unit = {
    val ds = MysqlEngine.getDataSource(
      "com.mysql.cj.jdbc.Driver",
      "localhost", 3306, "link_shorten", "root", "rever1234"
    )
    Utils.using(ds.getConnection())(conn => {})
      val conn =  ds.getConnection()
      val stm = conn.createStatement()
      val rs = stm.executeQuery("SELECT * FROM `link_shorten`.`tbl_shortlink`")
      assert(rs.next())
  }
}
