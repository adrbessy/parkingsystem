package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dao {

  public static final Logger logger = LogManager.getLogger("DAO");
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();
  public Connection con = null;
  PreparedStatement ps = null;

  /**
   * set up the database.
   * 
   * @param dbConstants a request
   */
  public void setUpDB(String dbConstants) {
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(dbConstants);
    } catch (Exception ex) {
      logger.error("Error instantiating database table: " + ex.getMessage());
    }
  }

  /**
   * close resources.
   * 
   */
  public void tearDown() {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        logger.error("The request cannot be closed", e);
      }
    }
    dataBaseConfig.closeConnection(con);
  }

}
