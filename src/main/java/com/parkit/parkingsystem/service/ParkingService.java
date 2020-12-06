package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParkingService {

  private static final Logger logger = LogManager.getLogger("ParkingService");

  private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

  private InputReaderUtil inputReaderUtil;
  private ParkingSpotDao parkingSpotDao;
  private TicketDao ticketDao;

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * parking service constructor.
   * 
   * @param inputReaderUtil an InputReaderUtil object.
   * @param parkingSpotDao  an ParkingSpotDAO object.
   * @param ticketDao       an ticketDao object.
   */
  public ParkingService(InputReaderUtil inputReaderUtil,
      ParkingSpotDao parkingSpotDao, TicketDao ticketDao) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDao = parkingSpotDao;
    this.ticketDao = ticketDao;
  }

  /**
   * function to save a new ticket when a user comes into the parking.
   * 
   */
  public void processIncomingVehicle() {
    try {
      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {

        parkingSpot.setAvailable(false);
        // allot this parking space and mark it's availability as
        // false
        parkingSpotDao.updateParking(parkingSpot);
        Date inTime = new Date();
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        String vehicleRegNumber = getVehicleRegNumber();
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        boolean recurringUser = ticketDao.checkRecurringUser(ticket.getVehicleRegNumber());
        ticket.setRecurringUser(recurringUser);
        ticketDao.saveTicket(ticket);
        System.out.println("Generated Ticket and saved in DB");
        if (ticket.getRecurringUser()) {
          System.out.println(
              "\nWelcome back! "
                  + "As a recurring user of our parking lot, you'll benefit from a 5% discount.");
        }
        System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
        System.out.println("Recorded in-time for vehicle number:"
            + vehicleRegNumber + " is:" + inTime);
      }
    } catch (Exception e) {
      logger.error("Unable to process incoming vehicle", e);
    }
  }

  public String getVehicleRegNumber() {
    System.out.println("\nPlease type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber();
  }

  /**
   * function to get the next parking number.
   * 
   */
  public ParkingSpot getNextParkingNumberIfAvailable() {
    int parkingNumber = 0;
    ParkingSpot parkingSpot = null;
    try {
      ParkingType parkingType = getVehicleType();
      parkingNumber = parkingSpotDao.getNextAvailableSlot(parkingType);
      if (parkingNumber > 0) {
        parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
      } else {
        throw new Exception("Error fetching parking number from DB. Parking slots might be full");
      }
    } catch (IllegalArgumentException ie) {
      logger.error("Error parsing user input for type of vehicle", ie);
    } catch (Exception e) {
      logger.error("Error fetching next available parking slot", e);
    }
    return parkingSpot;
  }

  /**
   * function to ask if the vehicle is a car or a bike.
   * 
   */
  public ParkingType getVehicleType() {
    System.out.println("\nPlease select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection();
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        // System.out.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }

  /**
   * function to process the leaving from the parking.
   * 
   */
  public void processExitingVehicle() {
    try {
      String vehicleRegNumber = getVehicleRegNumber();
      Ticket ticket = ticketDao.getTicket(vehicleRegNumber);
      Date outTime = new Date();
      ticket.setOutTime(outTime);
      double price = fareCalculatorService.calculateFare(ticket.getOutTime(), ticket.getInTime(),
          ticket.getParkingSpot().getParkingType(), ticket.getRecurringUser());
      price = Math.round(price * 100.0) / 100.0;
      ticket.setPrice(price);
      if (ticketDao.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDao.updateParking(parkingSpot);
        double priceToPay = ticket.getPrice();
        if (priceToPay == 0.0) {
          System.out.println("\nNothing to pay!");
        } else {
          System.out.println("\nPlease pay the parking fare:" + priceToPay);
        }
        System.out.println(
            "Recorded out-time for vehicle number:"
                + ticket.getVehicleRegNumber() + " is:" + outTime);
      } else {
        System.out.println("Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      logger.error("Unable to process exiting vehicle", e);
    }
  }
}
