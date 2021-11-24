package com.axelor.apps.helpdesk.db.repo;

import com.axelor.apps.helpdesk.db.Ticket;
import com.axelor.db.JpaRepository;

public class TicketRepository extends JpaRepository<Ticket> {

	public TicketRepository() {
		super(Ticket.class);
	}

	public static final int STATUS_NEW = 0;
	public static final int STATUS_IN_PROGRESS = 1;
	public static final int STATUS_RESOLVED = 2;
	public static final int STATUS_CLOSED = 3;
}

