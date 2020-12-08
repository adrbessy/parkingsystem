package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

}
