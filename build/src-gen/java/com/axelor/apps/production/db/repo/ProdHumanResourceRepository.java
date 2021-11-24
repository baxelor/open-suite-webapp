package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.ProdHumanResource;
import com.axelor.db.JpaRepository;

public class ProdHumanResourceRepository extends JpaRepository<ProdHumanResource> {

	public ProdHumanResourceRepository() {
		super(ProdHumanResource.class);
	}

}

