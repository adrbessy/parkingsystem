package com.parkit.parkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@Test
	public void testCalculateFareCar() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void testCalculateFareBike() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
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
	public void testCalculateFareBikeWithLessThanOneHourParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWithLessThanOneHourParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith15HoursParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (15 * 60 * 60 * 1000));// 15 hours parking time should give 3/4th
																			// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((15 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWithADayParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith25HoursParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 60 * 1000));// 25 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((25 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith39HoursParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (39 * 60 * 60 * 1000));// 39 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((39 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith50HoursParkingTime() {
		// ARRANGE
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (50 * 60 * 60 * 1000));// 50 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals((50 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
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
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals(((1 * Fare.CAR_RATE_PER_HOUR) - 5 * (1 * Fare.CAR_RATE_PER_HOUR) / 100), ticket.getPrice()); // 5%
		// discount
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
		fareCalculatorService.calculateFare(ticket);

		// ASSERT
		assertEquals(((1 * Fare.BIKE_RATE_PER_HOUR) - 5 * (1 * Fare.BIKE_RATE_PER_HOUR) / 100), ticket.getPrice()); // 5%
		// discount
	}

}
