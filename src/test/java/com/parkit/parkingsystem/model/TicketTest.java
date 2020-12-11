package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;
import com.parkit.parkingsystem.constants.ParkingType;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketTest {

  Ticket ticket;

  @BeforeEach
  private void setUp() {
    ticket = new Ticket();
  }

  @Test
  void getIdTest() {
    ticket.setId(1);
    assertThat(ticket.getId()).isEqualTo(1);
  }

  @Test
  void getParkingSpotTest() {
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setParkingSpot(parkingSpot);
    assertThat(ticket.getParkingSpot()).isEqualTo(parkingSpot);
  }

  @Test
  void getVehicleRegNumberTest() {
    ticket.setVehicleRegNumber("ABCDEF");
    assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");
  }

  @Test
  void getPriceTest() {
    ticket.setPrice(1.0);
    assertThat(ticket.getPrice()).isEqualTo(1.0);
  }

  @Test
  void getInTimeTest() {
    Date date = new Date();
    ticket.setInTime(date);
    assertThat(ticket.getInTime()).isEqualTo(date);
  }

  @Test
  void getOutTimeTest() {
    Date date = new Date();
    ticket.setOutTime(date);
    assertThat(ticket.getOutTime()).isEqualTo(date);
  }

  @Test
  void getRecurringUserTest() {
    ticket.setRecurringUser(true);
    assertThat(ticket.isRecurringUser()).isEqualTo(true);
  }

}
