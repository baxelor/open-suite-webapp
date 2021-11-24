package com.axelor.apps.hr.db.repo;

import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.db.JpaRepository;

public class TimesheetLineRepository extends JpaRepository<TimesheetLine> {

	public TimesheetLineRepository() {
		super(TimesheetLine.class);
	}

}

