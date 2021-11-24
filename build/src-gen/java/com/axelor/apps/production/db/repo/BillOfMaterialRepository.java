package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.BillOfMaterial;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class BillOfMaterialRepository extends JpaRepository<BillOfMaterial> {

	public BillOfMaterialRepository() {
		super(BillOfMaterial.class);
	}

	public BillOfMaterial findByName(String name) {
		return Query.of(BillOfMaterial.class)
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

