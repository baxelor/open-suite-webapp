package com.axelor.apps.production.db.repo;

import com.axelor.apps.production.db.ProductionOrder;
import com.axelor.db.JpaRepository;

public class ProductionOrderRepository extends JpaRepository<ProductionOrder> {

	public ProductionOrderRepository() {
		super(ProductionOrder.class);
	}

	// STATUS SELECT
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_PLANNED = 2;
	public static final int STATUS_STARTED = 3;
	public static final int STATUS_CANCELED= 4;
	public static final int STATUS_COMPLETED = 5;
}

