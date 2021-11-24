package com.axelor.apps.project.db.repo;

import com.axelor.apps.project.db.ProjectTaskCategory;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ProjectTaskCategoryRepository extends JpaRepository<ProjectTaskCategory> {

	public ProjectTaskCategoryRepository() {
		super(ProjectTaskCategory.class);
	}

	public ProjectTaskCategory findByName(String name) {
		return Query.of(ProjectTaskCategory.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

