package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.AppProject;
import com.axelor.db.JpaRepository;

public class AppProjectRepository extends JpaRepository<AppProject> {

	public AppProjectRepository() {
		super(AppProject.class);
	}

}

