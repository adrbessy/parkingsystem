package com.parkit.parkingsystem.constants;

public class DBconstants {

  public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) "
      + "from parking where AVAILABLE = true and TYPE = ?";
  public static final String UPDATE_PARKING_SPOT = "update parking set available = ? "
      + "where PARKING_NUMBER = ?";

  public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, "
      + "PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
  public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
  public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, "
      + "t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and "
      + "t.VEHICLE_REG_NUMBER=? order by t.IN_TIME desc limit 1";

  public static final String GET_VEHICLE_REG_NUMBER = "select VEHICLE_REG_NUMBER from ticket";

  public static final String DELETE_ROWS = "delete from ticket order by ID desc limit 2";

  public static final String COUNT_OCCURRENCES_OF_ONE_VEHICLE_REG_NUMBER = "select "
      + "count(VEHICLE_REG_NUMBER) from ticket where VEHICLE_REG_NUMBER = ?";

}
