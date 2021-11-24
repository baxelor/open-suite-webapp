package com.axelor.apps.hr.db.repo;

import com.axelor.apps.hr.db.Employee;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class EmployeeRepository extends JpaRepository<Employee> {

	public EmployeeRepository() {
		super(Employee.class);
	}

	public Employee findByName(String name) {
		return Query.of(Employee.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	public static final String TIME_PREFERENCE_DAYS = "days";
	public static final String TIME_PREFERENCE_HOURS = "hours";
	public static final String TIME_PREFERENCE_MINUTES = "minutes";

	public static final String SEX_F = "F";
	public static final String SEX_M = "M";

	public static final int TIMESHEET_PROJECT = 1;
	public static final int TIMESHEET_MANUF_ORDER = 2;
}

