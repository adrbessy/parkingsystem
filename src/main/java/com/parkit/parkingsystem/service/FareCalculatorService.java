package com.parkit.parkingsystem.service;

import java.util.Calendar;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public static final int sixtyMinutes = 60;
	public static final int twentyFourHours = 24;

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		Calendar in = Calendar.getInstance();
		in.setTime(ticket.getInTime());
		int inDay = in.get(Calendar.DAY_OF_MONTH);
		int inHour = in.get(Calendar.HOUR_OF_DAY);
		int inMinute = in.get(Calendar.MINUTE);
		inMinute = inHour * sixtyMinutes + inMinute;
		Calendar out = Calendar.getInstance();
		out.setTime(ticket.getOutTime());
		int outDay = out.get(Calendar.DAY_OF_MONTH);
		int outHour = out.get(Calendar.HOUR_OF_DAY);
		int outMinute = out.get(Calendar.MINUTE);
		outMinute = outHour * sixtyMinutes + outMinute;

		double duration;
		if (inMinute >= outMinute) {
			duration = (outDay - inDay) * twentyFourHours - inMinute / sixtyMinutes + outMinute / sixtyMinutes;
		} else {
			duration = outMinute - inMinute;
			duration = (outDay - inDay) * twentyFourHours + duration / sixtyMinutes;
		}

		if (duration < 0.5) {
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {

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
			}
		}
	}
}