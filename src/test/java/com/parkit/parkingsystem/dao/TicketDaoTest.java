package com.parkit.parkingsystem.dao;

import java.sql.ResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketDaoTest {

  TicketDao ticketDao;

  @Mock
  ResultSet rs;

  @BeforeEach
  private void setUp() {
    ticketDao = new TicketDao();
  }
  /*
   * @Test public void testProcessResultGetTicket() { try {
   * Mockito.when(rs.next()).thenReturn(true); Mockito.when(rs.getInt(1));
   * Mockito.when(rs.getInt(2)); Mockito.when(rs.getDouble(3));
   * Mockito.when(rs.getTimestamp(4)); Mockito.when(rs.getTimestamp(5));
   * Mockito.when(rs.getString(6));
   * 
   * ticketDao.processResultGetTicket(rs, ticket, vehicleRegNumber);
   * 
   * } }
   */

}
