package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@ParameterizedTest(name = "Test for {0} minutes.")
	@ValueSource(ints = { 45, 60, 900, 1440, 1500, 2340, 3000 })
	public void testCalculateFareCar(int arg) {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(((double) arg) / 60 * Fare.CAR_RATE_PER_HOUR);
	}

	@ParameterizedTest(name = "Test for {0} minutes.")
	@ValueSource(ints = { 0, 1, 5, 25, 29 })
	public void calculateFareCarUnderLess30MinutesTest(int arg) {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(0);
	}

	@ParameterizedTest(name = "Test for {0} minutes.")
	@ValueSource(ints = { 45, 60, 900, 1440, 1500, 2340, 3000 })
	public void testCalculateFareBike(int arg) {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(((double) arg) / 60 * Fare.BIKE_RATE_PER_HOUR);
	}

	@ParameterizedTest(name = "Test for {0} minutes.")
	@ValueSource(ints = { 0, 1, 5, 25, 29 })
	public void calculateFareBikeUnderLess30MinutesTest(int arg) {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (arg * 60 * 1000L));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(0);
	}

	@Test
	public void testCalculateFareUnkownType() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void testCalculateFareBikeWithFutureInTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ASSERT
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void testCalculateFareCarWithOneRecurringUser() {
		// ARRANGE
		Date inTime = new Date();
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (1 * 60 * 60 * 1000)); // 1 hour parking time should give 24 *
																			// parking fare per hour
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setOutTime(outTime);
		ticket.setRecurringUser(true);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(((1 * Fare.CAR_RATE_PER_HOUR) - 5 * (1 * Fare.CAR_RATE_PER_HOUR) / 100));
		// 5% discount
	}

	@Test
	public void testCalculateFareBikeWithOneRecurringUser() {
		// ARRANGE
		Date inTime = new Date();
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (1 * 60 * 60 * 1000)); // 1 hour parking time should give 24 *
																			// parking fare per hour
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setOutTime(outTime);
		ticket.setRecurringUser(true);

		// ACT
		double price = fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertThat(price).isEqualTo(((1 * Fare.BIKE_RATE_PER_HOUR) - 5 * (1 * Fare.BIKE_RATE_PER_HOUR) / 100));
		// 5% discount
	}

}
