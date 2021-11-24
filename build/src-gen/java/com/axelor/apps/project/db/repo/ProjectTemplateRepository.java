package com.axelor.apps.project.db.repo;

import com.axelor.apps.project.db.ProjectTemplate;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ProjectTemplateRepository extends JpaRepository<ProjectTemplate> {

	public ProjectTemplateRepository() {
		super(ProjectTemplate.class);
	}

	public ProjectTemplate findByCode(String code) {
		return Query.of(ProjectTemplate.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public ProjectTemplate findByName(String name) {
		return Query.of(ProjectTemplate.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

