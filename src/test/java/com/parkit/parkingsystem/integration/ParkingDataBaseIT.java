package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		int nextAvailableSlot = parkingService.getNextParkingNumberIfAvailable().getId();

		// ACT
		parkingService.processIncomingVehicle();

		// ASSERT
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertEquals(true, ticket != null);
		assertEquals(nextAvailableSlot + 1, parkingService.getNextParkingNumberIfAvailable().getId());
		assertEquals(false, ticket.getParkingSpot().isAvailable());
	}

	@Test
	public void testParkingLotExit() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		// ACT
		parkingService.processExitingVehicle();

		// ASSERT
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertEquals(true, ticket.getPrice() >= 0);
		assertEquals(true, ticket.getOutTime() != null);
	}

	@Test
	void checkRecurringUserTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setParkingSpot(parkingSpot);
		ticket.setId(1);
		ticket.setPrice(0);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticketDAO.saveTicket(ticket);

		boolean rep = ticketDAO.checkRecurringUser("ABCDEF");

		assertTrue(rep);
	}

	@Test
	void checkNotRecurringUserTest() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		Date inTime = new Date();
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setParkingSpot(parkingSpot);
		ticket.setId(1);
		ticket.setPrice(0);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		ticketDAO.saveTicket(ticket);

		boolean rep = ticketDAO.checkRecurringUser("ABCEEE");

		assertFalse(rep);
	}

	@Test
	@DisplayName("Verify that the next available spot for a CAR is spot 1 for empty DB")
	public void getNextAvailableSpotCar_TEST() {
		parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(1);
	}

	@Test
	@DisplayName("Verify that the next available spot for BIKE is spot 4 for empty DB")
	public void getNextAvailableSpotBike_TEST() {
		parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(4);
	}

	@Test
	@DisplayName("Verify that when updating parking, will get the next available CAR spot")
	public void updateParkingSpotCar_TEST() {
		// GIVEN
		parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		// THEN
		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isTrue();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).isEqualTo(2);
	}

	@Test
	@DisplayName("Verify that when updating parking, will get the next available BIKE spot")
	public void updateParkingSpotBike_TEST() {
		// GIVEN
		parkingSpotDAO.dataBaseConfig = new DataBaseTestConfig();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		// THEN
		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isTrue();
		assertThat(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).isEqualTo(5);

	}

}
