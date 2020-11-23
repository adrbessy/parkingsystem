package com.parkit.parkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();
	private static final Logger logger = LogManager.getLogger("FareCalculatorServiceTest");

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
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void testCalculateFareBike() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	@Test
	public void testCalculateFareUnkownType() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void testCalculateFareBikeWithFutureInTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void testCalculateFareBikeWithLessThanOneHourParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																		// parking fare
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
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
		fareCalculatorService.calculateFare(ticket);
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
		fareCalculatorService.calculateFare(ticket);
		assertEquals((15 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWithADayParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith25HoursParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 60 * 1000));// 25 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((25 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith35HoursParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (39 * 60 * 60 * 1000));// 39 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((39 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWith50HoursParkingTime() {
		Date inTime = new Date();
		inTime.setTime(System.currentTimeMillis() - (50 * 60 * 60 * 1000));// 50 hours parking time should give 24 *
																			// parking fare per hour
		Date outTime = new Date();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals((50 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void testCalculateFareCarWithOneRecurringUser() {

		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setInt(1, 3);
			ps.setString(2, "XXXXXXXXX");
			ps.setDouble(3, 0);
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (1 * 60 * 60 * 1000)));
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.execute();
		} catch (Exception ex) {
			logger.error("Error setting a ticket", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}

		TicketDAO ticketDAO = new TicketDAO();
		Date inTime = new Date();
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (1 * 60 * 60 * 1000)); // 1 hour parking time should give 24 *
																			// parking fare per hour
		ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR, false);
		ticket.setVehicleRegNumber("XXXXXXXXX");
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticketDAO.saveTicket(ticket);
		ticket.setRecurringUser(ticketDAO.checkRecurringUser(ticket));
		ticket.setOutTime(outTime);
		fareCalculatorService.calculateFare(ticket);
		double price = ticket.getPrice();

		Connection con2 = null;
		try {
			con2 = dataBaseConfig.getConnection();
			PreparedStatement ps = con2.prepareStatement(DBConstants.DELETE_ROWS);
			ps.execute();
		} catch (Exception ex) {
			logger.error("Error deleting tickets", ex);
		} finally {
			dataBaseConfig.closeConnection(con2);
		}

		assertEquals(((1 * Fare.CAR_RATE_PER_HOUR) - 5 * (1 * Fare.CAR_RATE_PER_HOUR) / 100), price); // 5%
																										// discount
	}

	@Test
	public void testCalculateFareBikeWithOneRecurringUser() {

		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setInt(1, 4);
			ps.setString(2, "XXXXXXXXX");
			ps.setDouble(3, 0);
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (1 * 60 * 60 * 1000)));
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.execute();
		} catch (Exception ex) {
			logger.error("Error setting a ticket", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}

		TicketDAO ticketDAO = new TicketDAO();
		Date inTime = new Date();
		Date outTime = new Date();
		outTime.setTime(System.currentTimeMillis() + (1 * 60 * 60 * 1000)); // 1 hour parking time should give 24 *
																			// parking fare per hour
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setVehicleRegNumber("XXXXXXXXX");
		ticket.setInTime(inTime);
		ticket.setParkingSpot(parkingSpot);
		ticketDAO.saveTicket(ticket);
		ticket.setRecurringUser(ticketDAO.checkRecurringUser(ticket));
		ticket.setOutTime(outTime);
		fareCalculatorService.calculateFare(ticket);
		double price = ticket.getPrice();

		Connection con2 = null;
		try {
			con2 = dataBaseConfig.getConnection();
			PreparedStatement ps = con2.prepareStatement(DBConstants.DELETE_ROWS);
			ps.execute();
		} catch (Exception ex) {
			logger.error("Error deleting tickets", ex);
		} finally {
			dataBaseConfig.closeConnection(con2);
		}

		assertEquals(((1 * Fare.BIKE_RATE_PER_HOUR) - 5 * (1 * Fare.BIKE_RATE_PER_HOUR) / 100), price); // 5%
																										// discount
	}

}
