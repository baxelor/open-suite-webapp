package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.OperationOrder;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class OperationOrderRepository extends JpaRepository<OperationOrder> {

	public OperationOrderRepository() {
		super(OperationOrder.class);
	}

	public OperationOrder findByName(String name) {
		return Query.of(OperationOrder.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// STATUS SELECT
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_CANCELED = 2;
	public static final int STATUS_PLANNED = 3;
	public static final int STATUS_IN_PROGRESS = 4;
	public static final int STATUS_STANDBY = 5;
	public static final int STATUS_FINISHED = 6;
}

