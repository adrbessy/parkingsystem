package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

  private ParkingService parkingService;

  private Ticket ticket;

  @Mock
  private InputReaderUtil inputReaderUtil;

  @Mock
  private ParkingSpotDao parkingSpotDao;

  @Mock
  private TicketDao ticketDao;

  @BeforeEach
  private void setUpPerTest() {
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDao, ticketDao);
  }

  @Test
  public void processIncomingVehicleTest() {
    // GIVEN
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDao.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    when(parkingSpotDao.updateParking(any(ParkingSpot.class))).thenReturn(true);
    when(ticketDao.checkRecurringUser("ABCDEF")).thenReturn(true);
    when(ticketDao.saveTicket(any(Ticket.class))).thenReturn(true);

    // THEN
    parkingService.processIncomingVehicle();
    verify(ticketDao, Mockito.times(1)).saveTicket(any(Ticket.class));
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
  public void processExitingVehicleTest() {
    // GIVEN
    ticket = new Ticket();
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber("ABCDEF");
    when(ticketDao.getTicket(anyString())).thenReturn(ticket);
    when(ticketDao.updateTicket(any(Ticket.class))).thenReturn(true);
    when(parkingSpotDao.updateParking(any(ParkingSpot.class))).thenReturn(true);

    // THEN
    parkingService.processExitingVehicle();
    verify(parkingSpotDao, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }

  @Test
  public void processExitingVehicleUpdateTicketFalseTest() {
    // GIVEN
    ticket = new Ticket();
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber("ABCDEF");
    when(ticketDao.getTicket(anyString())).thenReturn(ticket);
    when(ticketDao.updateTicket(any(Ticket.class))).thenReturn(false);

    // THEN
    parkingService.processExitingVehicle();
    verify(parkingSpotDao, Mockito.times(0)).updateParking(any(ParkingSpot.class));
  }

  @Test
  public void getVehicleRegNumberTest() {
    // GIVEN
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

    // THEN
    assertThat(parkingService.getVehicleRegNumber()).isEqualTo("ABCDEF");
    verify(inputReaderUtil).readVehicleRegistrationNumber();
  }

  @Test
  public void getNextParkingNumberIfNotAvailableTest() {
    // GIVEN
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDao.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

    // THEN
    assertThat(parkingService.getNextParkingNumberIfAvailable()).isEqualTo(null);
  }

  @Test
  public void getNextParkingNumberIfAvailableTest() {
    // GIVEN
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDao.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

    // THEN
    assertThat(parkingService.getNextParkingNumberIfAvailable()).isNotEqualTo(null);
  }

}
