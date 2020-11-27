package com.parkit.parkingsystem.service;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;

import com.parkit.parkingsystem.util.InputReaderUtil;

class InteractiveShellTest {

	// private static InteractiveShell interactiveShell;

	@Mock
	InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {
		// interactiveShell = new InteractiveShell();
	}

	/*
	 * @Test void testLoadInterface() { ParkingService parkingService =
	 * mock(ParkingService.class);
	 * 
	 * // GIVEN when(inputReaderUtil.readSelection()).thenReturn(1);
	 * 
	 * // ACT // InteractiveShell interactiveShell = new InteractiveShell();
	 * InteractiveShell.loadInterface();
	 * 
	 * verify(parkingService, times(1)).processIncomingVehicle(); }
	 */

}
