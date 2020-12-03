package com.parkit.parkingsystem.service;

import java.util.Calendar;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;

public class FareCalculatorService {

	public double calculateFare(Date outTime, Date Intime, ParkingType parkingType, boolean recurringUser) {
		if ((outTime == null) || (outTime.before(Intime))) {
			throw new IllegalArgumentException("Out time provided is incorrect");
		}
		Calendar in = Calendar.getInstance();
		in.setTime(Intime);
		Calendar out = Calendar.getInstance();
		out.setTime(outTime);

		double diffInMillisec = out.getTimeInMillis() - in.getTimeInMillis();
		double diffInHours = diffInMillisec / (60 * 60 * 1000);

		if (diffInHours < 0.5) {
			return 0;
		} else {
			switch (parkingType) {
			case CAR: {

				if (recurringUser) {
					return ((diffInHours * Fare.CAR_RATE_PER_HOUR) - 5 * (diffInHours * Fare.CAR_RATE_PER_HOUR) / 100);
				} else {
					return (diffInHours * Fare.CAR_RATE_PER_HOUR);
				}
			}
			case BIKE: {
				if (recurringUser) {
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