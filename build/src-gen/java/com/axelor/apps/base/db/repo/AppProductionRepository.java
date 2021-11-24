package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.AppProduction;
import com.axelor.db.JpaRepository;

public class AppProductionRepository extends JpaRepository<AppProduction> {

	public AppProductionRepository() {
		super(AppProduction.class);
	}

}

