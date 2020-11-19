package com.parkit.parkingsystem.service;

import java.util.Calendar;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		Calendar in = Calendar.getInstance();
		in.setTime(ticket.getInTime());
		int inHour = in.get(Calendar.HOUR_OF_DAY);
		int inMinute = in.get(Calendar.MINUTE);
		inMinute = inHour * 60 + inMinute;
		Calendar out = Calendar.getInstance();
		out.setTime(ticket.getOutTime());
		int outHour = out.get(Calendar.HOUR_OF_DAY);
		int outMinute = out.get(Calendar.MINUTE);
		outMinute = outHour * 60 + outMinute;

		// TODO: Some tests are failing here. Need to check if this logic is correct
		double duration = outMinute - inMinute;
		duration = duration / 60;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}