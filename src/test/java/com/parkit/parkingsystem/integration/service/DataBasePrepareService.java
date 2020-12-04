package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBasePrepareService {

  DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

  /**
   * delete all entries in the test database.
   * 
   */
  public void clearDataBaseEntries() {
    Connection connection = null;
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    try {
      connection = dataBaseTestConfig.getConnection();

      // set parking entries to available
      ps = connection.prepareStatement("update parking set available = true");
      ps.execute();

      // clear ticket entries;
      ps2 = connection.prepareStatement("truncate table ticket");
      ps2.execute();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (ps != null) {
        try {
          ps.close();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (ps2 != null) {
        try {
          ps2.close();

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      dataBaseTestConfig.closeConnection(connection);
    }
  }

}
