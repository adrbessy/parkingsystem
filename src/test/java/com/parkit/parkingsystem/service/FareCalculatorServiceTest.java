package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FareCalculatorServiceTest {

  private static FareCalculatorService fareCalculatorService;
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  /**
   * test to calculate the fare for a car with different times.
   * 
   */
  @ParameterizedTest(name = "Test for {0} minutes.")
  @ValueSource(ints = { 30, 45, 60, 900, 1440, 1500, 2340, 10000, 100000 })
  public void testCalculateFareCar(int arg) {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
    Date outTime = new Date();

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.CAR, false);

    // ASSERT
    assertThat(price).isEqualTo(((double) arg) / 60 * Fare.CAR_RATE_PER_HOUR);
  }

  /**
   * test to calculate the fare for a car with different times.
   * 
   */
  @ParameterizedTest(name = "Test for {0} minutes.")
  @ValueSource(ints = { 0, 1, 5, 25, 29 })
  public void calculateFareCarUnderLess30MinutesTest(int arg) {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
    Date outTime = new Date();

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.CAR, false);

    // ASSERT
    assertThat(price).isEqualTo(0);
  }

  /**
   * test to calculate the fare for a bike with different times.
   * 
   */
  @ParameterizedTest(name = "Test for {0} minutes.")
  @ValueSource(ints = { 30, 45, 60, 900, 1440, 1500, 2340, 10000, 100000 })
  public void testCalculateFareBike(int arg) {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
    Date outTime = new Date();

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.BIKE, false);

    // ASSERT
    assertThat(price).isEqualTo(((double) arg) / 60 * Fare.BIKE_RATE_PER_HOUR);
  }

  /**
   * test to calculate the fare for a bike with different times.
   * 
   */
  @ParameterizedTest(name = "Test for {0} minutes.")
  @ValueSource(ints = { 0, 1, 5, 25, 29 })
  public void calculateFareBikeUnderLess30MinutesTest(int arg) {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
    Date outTime = new Date();

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.BIKE, false);

    // ASSERT
    assertThat(price).isEqualTo(0);
  }

  @Test
  public void testCalculateFareUnkownType() {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();

    // ASSERT
    assertThrows(NullPointerException.class,
        () -> fareCalculatorService.calculateFare(outTime, inTime, null, false));
  }

  @Test
  public void testCalculateFareBikeWithFutureInTime() {
    // ARRANGE
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
    Date outTime = new Date();

    // ASSERT
    assertThrows(IllegalArgumentException.class,
        () -> fareCalculatorService.calculateFare(outTime, inTime, ParkingType.BIKE, false));
  }

  @Test
  public void testCalculateFareCarWithOneRecurringUser() {
    // ARRANGE
    Date inTime = new Date();
    Date outTime = new Date();
    outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.CAR, true);

    // ASSERT
    assertThat(Math.round(price * 100.0) / 100.0).isEqualTo(Math.round(((Fare.CAR_RATE_PER_HOUR)
        - 5 * (Fare.CAR_RATE_PER_HOUR) / 100) * 100.0) / 100.0);
    // 5% discount
  }

  @Test
  public void testCalculateFareBikeWithOneRecurringUser() {
    // ARRANGE
    Date inTime = new Date();
    Date outTime = new Date();
    outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));

    // ACT
    double price = fareCalculatorService.calculateFare(outTime, inTime, ParkingType.BIKE, true);

    // ASSERT
    assertThat(price).isEqualTo(((Fare.BIKE_RATE_PER_HOUR)
        - 5 * (Fare.BIKE_RATE_PER_HOUR) / 100));
    // 5% discount
  }

}
