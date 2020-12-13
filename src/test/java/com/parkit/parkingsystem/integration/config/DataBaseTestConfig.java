package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseTestConfig extends DataBaseConfig {

  private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

  /**
   * connect to the test database.
   * 
   */
  @Override
  public Connection getConnection() throws ClassNotFoundException, SQLException {

    ResourceBundle rb = ResourceBundle.getBundle("data");
    String user = rb.getString("username");
    String pass = rb.getString("password");
    String port = rb.getString("port");

    logger.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:" + port + "/test", user, pass);
  }

}
