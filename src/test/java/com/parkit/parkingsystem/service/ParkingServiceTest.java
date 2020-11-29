package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	ParkingService parkingService;

	private Ticket ticket;

	@Mock
	InputReaderUtil inputReaderUtil;

	@Mock
	ParkingSpotDAO parkingSpotDAO;

	@Mock
	TicketDAO ticketDAO;

	@BeforeEach
	private void setUp() {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ticket = new Ticket();
	}

	@Test
	public void processExitingVehicleTest() {
		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		// THEN
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void processExitingVehicleUpdateTicketFalseTest() {
		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		// THEN
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
	}

	/*
	 * @Test public void testCheckRecurringUser() { // ARRANGE
	 * ticket.setRecurringUser(false); ArrayList<String> str = new
	 * ArrayList<String>(); str.add("ABCDE"); str.add("ABCDE");
	 * ticket.setVehicleRegNumberList(str); ticket.setVehicleRegNumber("ABCDE");
	 * 
	 * // ACT boolean recurringUser = parkingService.checkRecurringUser(ticket);
	 * 
	 * // ASSERT assertTrue(recurringUser); }
	 * 
	 * @Test public void testCheckNoRecurringUser() { // ARRANGE
	 * ticket.setRecurringUser(false); ArrayList<String> str = new
	 * ArrayList<String>(); str.add("ABCDE"); str.add("ABCFF");
	 * ticket.setVehicleRegNumberList(str); ticket.setVehicleRegNumber("ABCFF");
	 * 
	 * // ACT boolean recurringUser = parkingService.checkRecurringUser(ticket);
	 * 
	 * // ASSERT assertFalse(recurringUser); }
	 */
	@Test
	public void testGetVehicleType1() {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);

		// THEN
		assertThat(parkingService.getVehicleType()).isEqualTo(ParkingType.CAR);
		verify(inputReaderUtil).readSelection();
	}

	@Test
	public void testGetVehicleType2() {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(2);

		// THEN
		assertThat(parkingService.getVehicleType()).isEqualTo(ParkingType.BIKE);
		verify(inputReaderUtil).readSelection();
	}

	@Test
	public void testGetVehicleTypeDefault() {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(3);

		// THEN
		assertThrows(IllegalArgumentException.class, () -> parkingService.getVehicleType());
		verify(inputReaderUtil).readSelection();
	}

	@Test
	public void testGetVehicleRegNumber() {
		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

		// THEN
		assertThat(parkingService.getVehicleRegNumber()).isEqualTo("ABCDEF");
		verify(inputReaderUtil).readVehicleRegistrationNumber();
	}

}
