package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {

	TicketDAO ticketDAO;

	@Mock
	Connection con;

	@Mock
	PreparedStatement ps;

	@BeforeEach
	private void setUp() {
		ticketDAO = new TicketDAO();
	}
	/*
	 * @Test void checkRecurringUserTest() throws Exception { Date inTime = new
	 * Date(); inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000L)); Date
	 * outTime = new Date(); //
	 * when(con.prepareStatement(anyString())).thenReturn(ps);
	 * doNothing().when(ps).setInt(1, 1); doNothing().when(ps).setString(2,
	 * "ABCDEF"); doNothing().when(ps).setDouble(3, 2.0);
	 * doNothing().when(ps).setTimestamp(4, new Timestamp(inTime.getTime()));
	 * doNothing().when(ps).setTimestamp(5, (outTime == null) ? null : (new
	 * Timestamp(outTime.getTime())));
	 * 
	 * ticketDAO.setData(1, "ABCDEF", 2.0, inTime, outTime);
	 * 
	 * verify(ps).setInt(1, 1); verify(ps).setString(2, "ABCDEF");
	 * verify(ps).setDouble(3, 2.0); verify(ps).setTimestamp(4, new
	 * Timestamp(inTime.getTime())); verify(ps).setTimestamp(5, (outTime == null) ?
	 * null : (new Timestamp(outTime.getTime())));
	 * 
	 * }
	 */
}
