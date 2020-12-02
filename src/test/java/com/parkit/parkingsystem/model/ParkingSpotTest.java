package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {

	ParkingSpot parkingSpot;

	@BeforeEach
	private void setUp() {
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	}

	@Test
	void getId_TEST() {
		assertThat(parkingSpot.getId()).isEqualTo(1);
	}

	@Test
	void getParkingType_TEST() {
		assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
	}

	@Test
	void isAvailable_TEST() {
		assertThat(parkingSpot.isAvailable()).isEqualTo(false);
	}

	@Test
	void setId_TEST() {
		parkingSpot.setId(2);
		assertThat(parkingSpot.getId()).isEqualTo(2);
	}

	@Test
	void setParkingType_TEST() {
		parkingSpot.setParkingType(ParkingType.BIKE);
		assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.BIKE);
	}

	@Test
	void setAvailable_TEST() {
		parkingSpot.setAvailable(true);
		assertThat(parkingSpot.isAvailable()).isEqualTo(true);
	}

}
