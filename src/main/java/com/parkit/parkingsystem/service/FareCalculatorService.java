package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import java.util.Calendar;
import java.util.Date;

public class FareCalculatorService {

  /**
   * calculate the fare when the user leave the parking.
   * 
   * @param outTime       the time when the user leaves
   * @param inTime        the time when the user arrives to the parking
   * @param parkingType   car or bike
   * @param recurringUser is recurring user?
   */
  public double calculateFare(Date outTime, Date inTime,
      ParkingType parkingType, boolean recurringUser) {
    if ((outTime == null) || (outTime.before(inTime))) {
      throw new IllegalArgumentException("Out time provided is incorrect");
    }
    Calendar in = Calendar.getInstance();
    in.setTime(inTime);
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
            return ((diffInHours * Fare.CAR_RATE_PER_HOUR)
                - 5 * (diffInHours * Fare.CAR_RATE_PER_HOUR) / 100);
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
        default:
          return 0;
      }
    }
  }
}