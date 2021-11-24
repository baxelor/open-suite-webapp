package com.axelor.apps.project.db.repo;

import com.axelor.apps.project.db.TaskTemplate;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class TaskTemplateRepository extends JpaRepository<TaskTemplate> {

	public TaskTemplateRepository() {
		super(TaskTemplate.class);
	}

	public TaskTemplate findByName(String name) {
		return Query.of(TaskTemplate.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

