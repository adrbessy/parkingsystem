package com.parkit.parkingsystem.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

  private static final Logger logger = LogManager.getLogger("DataBaseConfig");

  /**
   * connect to the database.
   * 
   */
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    Properties prop = new Properties();

    try {
      InputStream input = new FileInputStream("../data.properties");
      prop.load(input);
      input.close();
    } catch (IOException e) {
      logger.error("Error while loading data.properties", e);
    }

    String user = prop.getProperty("username");
    String pass = prop.getProperty("password");
    String port = prop.getProperty("port");
    System.out.println("");
    logger.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:" + port + "/prod", user, pass);
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
