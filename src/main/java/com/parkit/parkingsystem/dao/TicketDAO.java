package com.parkit.parkingsystem.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAO extends DAO {

	public boolean saveTicket(Ticket ticket) {
		try {
			setUpDB(DBConstants.SAVE_TICKET);
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			return ps.execute();
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			tearDown();
		}
		return false;
	}

	public Ticket getTicket(String vehicleRegNumber) {
		Ticket ticket = null;
		try {
			setUpDB(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}

	public boolean updateTicket(Ticket ticket) {
		try {
			setUpDB(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			tearDown();
		}
		return false;
	}

	public boolean checkRecurringUser(String vehicleRegNumber) {
		try {
			setUpDB(DBConstants.COUNT_OCCURRENCES_OF_ONE_VEHICLE_REG_NUMBER);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int nb = rs.getInt(1);
				if (nb > 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception ex) {
			logger.error("Error fetching recurrent user", ex);
		} finally {
			tearDown();
		}
		return false;
	}
}
