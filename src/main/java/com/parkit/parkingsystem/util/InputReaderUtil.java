package com.parkit.parkingsystem.util;

import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

  private static Scanner scan = new Scanner(System.in);
  private static final Logger logger = LogManager.getLogger("InputReaderUtil");

  /**
   * ask to write the kind of vehicle (1 for car or 2 for bike).
   * 
   */
  public int readSelection() {
    try {
      int input = Integer.parseInt(scan.nextLine());
      return input;
    } catch (Exception e) {
      // logger.error("Error while reading user input from Shell", e);
      // System.out.println("Error reading input. Please enter valid number for
      // proceeding further");
      return -1;
    }
  }

  /**
   * ask to write the vehicle registration number.
   * 
   */
  public String readVehicleRegistrationNumber() {
    try {
      String vehicleRegNumber = scan.nextLine();
      if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
        throw new IllegalArgumentException("Invalid input provided");
      }
      return vehicleRegNumber;
    } catch (Exception e) {
      logger.error("Error while reading user input from Shell", e);
      System.out.println("Error reading input. "
          + "Please enter a valid string for vehicle registration number");
      throw e;
    }
  }

}
