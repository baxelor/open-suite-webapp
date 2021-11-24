package com.axelor.apps.supplychain.db.repo;

import com.axelor.apps.supplychain.db.SupplyChainConfig;
import com.axelor.db.JpaRepository;

public class SupplyChainConfigRepository extends JpaRepository<SupplyChainConfig> {

	public SupplyChainConfigRepository() {
		super(SupplyChainConfig.class);
	}

	// Default estimated Date
	public static final int CURRENT_DATE = 0;
	public static final int EMPTY_DATE = 1;
	public static final int CURRENT_DATE_PLUS_DAYS = 2;

	// Sale order reservation date select
	public static final int SALE_ORDER_CONFIRMATION_DATE = 0;
	public static final int SALE_ORDER_SHIPPING_DATE = 1;
}

