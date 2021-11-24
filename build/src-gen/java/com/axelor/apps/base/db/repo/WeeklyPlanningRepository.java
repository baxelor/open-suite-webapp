package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.WeeklyPlanning;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class WeeklyPlanningRepository extends JpaRepository<WeeklyPlanning> {

	public WeeklyPlanningRepository() {
		super(WeeklyPlanning.class);
	}

	public WeeklyPlanning findByName(String name) {
		return Query.of(WeeklyPlanning.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	public static final int WEEKLY_PLANNING_HUMAN_RESOURCES = 1;
	public static final int WEEKLY_PLANNING_MACHINE = 2;
}

