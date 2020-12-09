package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.DBconstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import java.sql.ResultSet;

public class ParkingSpotDao extends Dao {

  /**
   * get the next available spot.
   * 
   * @param parkingType car or bike
   */
  public int getNextAvailableSlot(ParkingType parkingType) {
    int result = -1;
    try {
      setUpDB(DBconstants.GET_NEXT_PARKING_SPOT);
      ps.setString(1, parkingType.toString());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      closeResultSet(rs);
      closePreparedStatement(ps);
      closeConnection(con);
    }
    return result;
  }

  /**
   * update the parking when the user arrives, then when he leaves.
   * 
   * @param parkingSpot a ParkingSpot object
   */
  public boolean updateParking(ParkingSpot parkingSpot) {
    // update the availability for that parking slot
    try {
      setUpDB(DBconstants.UPDATE_PARKING_SPOT);
      ps.setBoolean(1, parkingSpot.isAvailable());
      ps.setInt(2, parkingSpot.getId());
      int updateRowCount = ps.executeUpdate();
      return (updateRowCount == 1);
    } catch (Exception ex) {
      logger.error("Error updating parking info", ex);
      return false;
    } finally {
      closePreparedStatement(ps);
      closeConnection(con);
    }
  }

}
