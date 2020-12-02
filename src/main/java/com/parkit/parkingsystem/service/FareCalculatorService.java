package com.parkit.parkingsystem.service;

import java.util.Calendar;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public double calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		Calendar in = Calendar.getInstance();
		in.setTime(ticket.getInTime());
		Calendar out = Calendar.getInstance();
		out.setTime(ticket.getOutTime());

		double diffInMillisec = out.getTimeInMillis() - in.getTimeInMillis();
		double diffInHours = diffInMillisec / (60 * 60 * 1000);

		if (diffInHours < 0.5) {
			return 0;
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {

				if (ticket.getRecurringUser()) {
					return ((diffInHours * Fare.CAR_RATE_PER_HOUR) - 5 * (diffInHours * Fare.CAR_RATE_PER_HOUR) / 100);
				} else {
					return (diffInHours * Fare.CAR_RATE_PER_HOUR);
				}
			}
			case BIKE: {
				if (ticket.getRecurringUser()) {
					return ((diffInHours * Fare.BIKE_RATE_PER_HOUR)
							- 5 * (diffInHours * Fare.BIKE_RATE_PER_HOUR) / 100);
				} else {
					return (diffInHours * Fare.BIKE_RATE_PER_HOUR);
				}
			}
			}
		}
		return 0;
	}
}