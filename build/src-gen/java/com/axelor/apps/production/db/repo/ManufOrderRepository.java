package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.ManufOrder;
import com.axelor.db.JpaRepository;

public class ManufOrderRepository extends JpaRepository<ManufOrder> {

	public ManufOrderRepository() {
		super(ManufOrder.class);
	}

	// STATUS SELECT
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_CANCELED = 2;
	public static final int STATUS_PLANNED = 3;
	public static final int STATUS_IN_PROGRESS = 4;
	public static final int STATUS_STANDBY = 5;
	public static final int STATUS_FINISHED = 6;
	public static final int STATUS_MERGED = 7;

	//TYPE SELECT
	public static final int TYPE_PRODUCTION = 1;
	public static final int TYPE_PERMANENT = 2;

	//TYPE SELECT
	public static final int TYPE_MAINTENANCE = 3;
}

