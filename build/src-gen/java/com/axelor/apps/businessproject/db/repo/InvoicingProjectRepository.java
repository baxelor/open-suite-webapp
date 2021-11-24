package com.axelor.apps.businessproject.db.repo;

import com.axelor.apps.businessproject.db.InvoicingProject;
import com.axelor.db.JpaRepository;

public class InvoicingProjectRepository extends JpaRepository<InvoicingProject> {

	public InvoicingProjectRepository() {
		super(InvoicingProject.class);
	}

	// STATUS SELECT
	public static final int STATUS_DRAFT = 1;
	public static final int
	STATUS_GENERATED =
	2;
	public static final int STATUS_VALIDATED = 3;
	public static final int
	STATUS_VENTILATED = 4;
	public static final int STATUS_CANCELED = 5;
}

