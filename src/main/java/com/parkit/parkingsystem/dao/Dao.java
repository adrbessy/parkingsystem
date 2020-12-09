package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dao {

  public static final Logger logger = LogManager.getLogger("DAO");
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();
  public Connection con = null;
  PreparedStatement ps = null;
  ResultSet rs = null;

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
   * disconnect to the database.
   * 
   */
  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  /**
   * close the PreparedStatement object.
   * 
   */
  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
   * close the ResultSet object.
   * 
   */
  public void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        logger.info("Closing Result Set");
      } catch (SQLException e) {
        logger.error("Error while closing result set", e);
      }
    }
  }

}
