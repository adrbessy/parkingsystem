package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketTest {

	Ticket ticket;

	@BeforeEach
	private void setUp() {
		ticket = new Ticket();
	}

	@Test
	void getId_TEST() {
		ticket.setId(1);
		assertThat(ticket.getId()).isEqualTo(1);
	}

}
