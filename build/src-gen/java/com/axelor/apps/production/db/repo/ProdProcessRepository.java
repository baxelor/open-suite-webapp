package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.ProdProcess;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ProdProcessRepository extends JpaRepository<ProdProcess> {

	public ProdProcessRepository() {
		super(ProdProcess.class);
	}

	public ProdProcess findByCode(String code) {
		return Query.of(ProdProcess.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public ProdProcess findByName(String name) {
		return Query.of(ProdProcess.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// STATUS SELECT
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_VALIDATED = 2;
	public static final int STATUS_APPLICABLE = 3;
	public static final int STATUS_OBSOLETE = 4;
}

