package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

class ParkingSpotDAOTest {

	ParkingSpotDAO parkingSpotDAO;
	DataBasePrepareService dataBasePrepareService;

	@BeforeEach
	private void setUp() {
		parkingSpotDAO = new ParkingSpotDAO();
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterEach
	private void closeUpPerTest() throws Exception {
		dataBasePrepareService.clearDataBaseEntries();
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
