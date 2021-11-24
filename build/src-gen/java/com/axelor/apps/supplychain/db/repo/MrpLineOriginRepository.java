package com.axelor.apps.supplychain.db.repo;

import com.axelor.apps.supplychain.db.MrpLineOrigin;
import com.axelor.db.JpaRepository;

public class MrpLineOriginRepository extends JpaRepository<MrpLineOrigin> {

	public MrpLineOriginRepository() {
		super(MrpLineOrigin.class);
	}

	//RELATED TO SELECT TYPE
			public static final String RELATED_TO_SALE_ORDER_LINE = "com.axelor.apps.sale.db.SaleOrderLine";
			public static final String RELATED_TO_PURCHASE_ORDER_LINE = "com.axelor.apps.purchase.db.PurchaseOrderLine";
			public static final String RELATED_TO_MRP_FORECAST = "com.axelor.apps.supplychain.db.MrpForecast";

	//RELATED TO SELECT TYPE PRODUCTION
			public static final String RELATED_TO_MANUFACTURING_ORDER = "com.axelor.apps.production.db.ManufOrder";
			public static final String RELATED_TO_OPERATION_ORDER = "com.axelor.apps.production.db.OperationOrder";
}

