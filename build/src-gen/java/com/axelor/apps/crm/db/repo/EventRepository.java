package com.axelor.apps.crm.db.repo;

import com.axelor.apps.crm.db.Event;
import com.axelor.db.JpaRepository;

public class EventRepository extends JpaRepository<Event> {

	public EventRepository() {
		super(Event.class);
	}

	// TYPE SELECT
	public static final int TYPE_EVENT = 0;
	public static final int TYPE_CALL = 1;
	public static final int TYPE_MEETING = 2;
	public static final int TYPE_TASK = 3;

	// STATUS SELECT
	public static final int STATUS_PLANNED = 1;
	public static final int STATUS_REALIZED = 2;
	public static final int STATUS_CANCELED = 3;
	public static final int STATUS_NOT_STARTED = 11;
	public static final int STATUS_ON_GOING = 12;
	public static final int STATUS_PENDING = 13;
	public static final int STATUS_FINISHED = 14;
	public static final int STATUS_REPORTED = 15;
}

