package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;
import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingSpotTest {

  ParkingSpot parkingSpot;

  @BeforeEach
  private void setUp() {
    parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
  }

  @Test
  void getIdTest() {
    assertThat(parkingSpot.getId()).isEqualTo(1);
  }

  @Test
  void getParkingTypeTest() {
    assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
  }

  @Test
  void isAvailableTest() {
    assertThat(parkingSpot.isAvailable()).isEqualTo(false);
  }

  @Test
  void setIdTest() {
    parkingSpot.setId(2);
    assertThat(parkingSpot.getId()).isEqualTo(2);
  }

  @Test
  void setParkingTypeTest() {
    parkingSpot.setParkingType(ParkingType.BIKE);
    assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
  }

  @Test
  void setAvailableTest() {
    parkingSpot.setAvailable(true);
    assertThat(parkingSpot.isAvailable()).isEqualTo(true);
  }

}
