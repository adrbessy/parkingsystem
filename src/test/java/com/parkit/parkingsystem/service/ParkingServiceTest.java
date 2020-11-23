package com.parkit.parkingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;
	private Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeAll
	private static void setUp() {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}
	/*
	 * @BeforeEach private void setUpPerTest() { try {
	 * when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	 * 
	 * ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false); Ticket
	 * ticket = new Ticket(); ticket.setInTime(new Date(System.currentTimeMillis() -
	 * (60 * 60 * 1000))); ticket.setParkingSpot(parkingSpot);
	 * ticket.setVehicleRegNumber("ABCDEF");
	 * when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	 * when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
	 * 
	 * when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
	 * 
	 * parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO,
	 * ticketDAO); } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Failed to set up test mock objects"); } }
	 * 
	 * @Test public void processExitingVehicleTest() {
	 * parkingService.processExitingVehicle(); verify(parkingSpotDAO,
	 * Mockito.times(1)).updateParking(any(ParkingSpot.class)); }
	 */

	@Test
	public void testCheckRecurringUser() {
		// ARRANGE
		ticket = new Ticket();
		ticket.setRecurringUser(false);
		ArrayList<String> str = new ArrayList<String>();
		str.add("ABCDE");
		str.add("ABCDE");
		ticket.setVehicleRegNumberList(str);
		ticket.setVehicleRegNumber("ABCDE");

		// ACT
		boolean recurringUser = parkingService.checkRecurringUser(ticket);

		// ASSERT
		assertEquals(true, recurringUser);
	}

	@Test
	public void testCheckNoRecurringUser() {
		// ARRANGE
		ticket = new Ticket();
		ticket.setRecurringUser(false);
		ArrayList<String> str = new ArrayList<String>();
		str.add("ABCDE");
		str.add("ABCFF");
		ticket.setVehicleRegNumberList(str);
		ticket.setVehicleRegNumber("ABCFF");

		// ACT
		boolean recurringUser = parkingService.checkRecurringUser(ticket);

		// ASSERT
		assertEquals(false, recurringUser);
	}

}
