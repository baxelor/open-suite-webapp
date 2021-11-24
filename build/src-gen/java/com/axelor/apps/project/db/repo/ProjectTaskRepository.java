package com.axelor.apps.project.db.repo;

import com.axelor.apps.project.db.ProjectTask;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ProjectTaskRepository extends JpaRepository<ProjectTask> {

	public ProjectTaskRepository() {
		super(ProjectTask.class);
	}

	public ProjectTask findByName(String name) {
		return Query.of(ProjectTask.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// STATUS SELECT
	public static final String STATUS_NEW = "new";
	public static final String STATUS_IN_PROGRESS = "in-progress";
	public static final String STATUS_CLOSED = "closed";
	public static final String STATUS_CANCELED = "canceled";

	// PRIORITY SELECT
	public static final String PRIORITY_LOW = "low";
	public static final String PRIORITY_NORMAL = "normal";
	public static final String PRIORITY_HIGHT = "high";
	public static final String PRIORITY_URGENT = "urgent";

	// TYPE SELECT
	public static final String TYPE_TASK = "task";
	public static final String TYPE_TICKET = "ticket";

	public static final Integer INVOICING_TYPE_TIME_SPENT= 1;
	public static final Integer INVOICING_TYPE_PACKAGE = 2;
	public static final Integer INVOICING_TYPE_NO_INVOICING = 3;

	// ASSIGNMENT
	public static final int ASSIGNMENT_CUSTOMER = 1;
	public static final int ASSIGNMENT_PROVIDER = 2;
}

