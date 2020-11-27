package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
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

	@Mock
	Logger logger;

	@BeforeEach
	private void setUp() {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		ticket = new Ticket();
	}

	@AfterEach
	public void undefTicket() {
		ticket = null;
	}

	/*
	 * @Test public void processIncomingVehicleTest() { // GIVEN
	 * when(inputReaderUtil.readSelection()).thenReturn(1);
	 * when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(3);
	 * when(parkingService.getVehicleRegNumber()).thenReturn("ABCDEF");
	 * when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
	 * when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
	 * when(ticketDAO.getAllVehicleRegNumber(any(Ticket.class))).thenReturn(true);
	 * when(parkingService.checkRecurringUser(any(Ticket.class))).thenReturn(true);
	 * 
	 * // THEN parkingService.processIncomingVehicle(); }
	 */

	/*
	 * @Test public void getNextParkingNumberIfAvailableTest() { // GIVEN
	 * when(inputReaderUtil.readSelection()).thenReturn(1);
	 * when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(3);
	 * 
	 * // THEN parkingService.getNextParkingNumberIfAvailable(); // don't know how
	 * to assert a new instance. }
	 */

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
	 * @Test public void processExitingVehicleFailTest() { try {
	 * parkingService.processExitingVehicle(); } catch (Exception ex) { //
	 * assertThat(ex.getMessage()).isEqualTo("Unable to process exiting vehicle");
	 * verify(logger).warn("Unable to process exiting vehicle"); } }
	 */

	@Test
	public void testCheckRecurringUser() {
		// ARRANGE
		ticket.setRecurringUser(false);
		ArrayList<String> str = new ArrayList<String>();
		str.add("ABCDE");
		str.add("ABCDE");
		ticket.setVehicleRegNumberList(str);
		ticket.setVehicleRegNumber("ABCDE");

		// ACT
		boolean recurringUser = parkingService.checkRecurringUser(ticket);

		// ASSERT
		assertTrue(recurringUser);
	}

	@Test
	public void testCheckNoRecurringUser() {
		// ARRANGE
		ticket.setRecurringUser(false);
		ArrayList<String> str = new ArrayList<String>();
		str.add("ABCDE");
		str.add("ABCFF");
		ticket.setVehicleRegNumberList(str);
		ticket.setVehicleRegNumber("ABCFF");

		// ACT
		boolean recurringUser = parkingService.checkRecurringUser(ticket);

		// ASSERT
		assertFalse(recurringUser);
	}

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

	/*
	 * @Test public void testGetNextParkingNumberIfAvailable() { // GIVEN
	 * when(parkingService.getVehicleType()).thenReturn(ParkingType.CAR);
	 * when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(3);
	 * 
	 * // THEN assertThat(parkingService.getNextParkingNumberIfAvailable())
	 * .isEqualTo(new ParkingSpot(3, ParkingType.CAR, true));
	 * verify(parkingService).getVehicleType();
	 * verify(parkingSpotDAO).getNextAvailableSlot(ParkingType.CAR); }
	 */

	/*
	 * @Test public void testProcessExitingVehicle() { Date inTime = new Date();
	 * inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	 * ticket.setInTime(inTime); ParkingSpot parkingSpot = new ParkingSpot(1,
	 * ParkingType.CAR, false); ticket.setParkingSpot(parkingSpot);
	 * 
	 * // GIVEN when(parkingService.getVehicleRegNumber()).thenReturn("ABCDEF");
	 * when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
	 * when(ticketDAO.updateTicket(ticket)).thenReturn(true);
	 * 
	 * // THEN assertThat(parkingService.getVehicleRegNumber()).isEqualTo("ABCDEF");
	 * verify(inputReaderUtil).readVehicleRegistrationNumber(); }
	 */

	// @Mock
	// parkingSpotDAO parkingSpotDAO;

	// @InjectMocks
	// EmailService emailService;

	/*
	 * @Captor ArgumentCaptor<ParkingSpot> parkingSpotCaptor;
	 * 
	 * @Test public void testProcessExitingVehicle() { int number = 1; ParkingType
	 * parkingType = ParkingType.CAR; boolean isAvailable = true; ParkingSpot
	 * parkingSpot = new ParkingSpot(number, parkingType, isAvailable);
	 * 
	 * verify(parkingSpotDAO).updateParking(parkingSpotCaptor.capture());
	 * ParkingSpot parkingSpotCaptorValue = parkingSpotCaptor.getValue();
	 * 
	 * }
	 */

}
