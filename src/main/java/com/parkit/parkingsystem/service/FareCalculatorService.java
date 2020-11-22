package com.parkit.parkingsystem.service;

import java.util.Calendar;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	/*
	 * private static final Logger logger2 =
	 * LogManager.getLogger("FareCalculatorService");
	 */

	/* public DataBaseConfig dataBaseConfig2 = new DataBaseConfig(); */

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		Calendar in = Calendar.getInstance();
		in.setTime(ticket.getInTime());
		int inDay = in.get(Calendar.DAY_OF_MONTH);
		int inHour = in.get(Calendar.HOUR_OF_DAY);
		int inMinute = in.get(Calendar.MINUTE);
		inMinute = inHour * 60 + inMinute;
		Calendar out = Calendar.getInstance();
		out.setTime(ticket.getOutTime());
		int outDay = out.get(Calendar.DAY_OF_MONTH);
		int outHour = out.get(Calendar.HOUR_OF_DAY);
		int outMinute = out.get(Calendar.MINUTE);
		outMinute = outHour * 60 + outMinute;

		double duration;
		if (inMinute >= outMinute) {
			duration = (outDay - inDay) * 24 - inMinute / 60 + outMinute / 60;
		} else {
			duration = outMinute - inMinute;
			duration = (outDay - inDay) * 24 + duration / 60;
		}

		// Calculate 5% discount if recurrent user:
		/*
		 * Connection con = null; ArrayList<String> VEHICLE_REG_NUMBER_List = new
		 * ArrayList<String>(); int numCount = 0; try { con =
		 * dataBaseConfig2.getConnection(); PreparedStatement ps =
		 * con.prepareStatement(DBConstants.GET_VEHICLE_REG_NUMBER); ResultSet rs =
		 * ps.executeQuery(); while (rs.next()) {
		 * VEHICLE_REG_NUMBER_List.add(rs.getString(1)); } if
		 * (!VEHICLE_REG_NUMBER_List.isEmpty()) {
		 * VEHICLE_REG_NUMBER_List.remove(VEHICLE_REG_NUMBER_List.size() - 1); } /* for
		 * (String thisNum : VEHICLE_REG_NUMBER_List) { if
		 * (thisNum.equals(myVehicleRegNumber)) numCount++; }
		 */
		/*
		 * }catch(
		 * 
		 * Exception ex) { logger2.error("Error fetching recurrent user", ex); }finally
		 * { dataBaseConfig2.closeConnection(con); }
		 */

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			/* String myVehicleRegNumber = ticket.getVehicleRegNumber(); */
			if (ticket.getRecurringUser()) {
				ticket.setPrice(
						(duration * Fare.CAR_RATE_PER_HOUR) - 5 * (duration * Fare.CAR_RATE_PER_HOUR) / 100);
			} else {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			}
			break;
		}
		case BIKE: {
			if (ticket.getRecurringUser()) {
				ticket.setPrice(
						(duration * Fare.BIKE_RATE_PER_HOUR) - 5 * (duration * Fare.BIKE_RATE_PER_HOUR) / 100);
			} else {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}