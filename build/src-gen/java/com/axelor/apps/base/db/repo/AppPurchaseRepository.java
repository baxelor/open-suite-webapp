package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.AppPurchase;
import com.axelor.db.JpaRepository;

public class AppPurchaseRepository extends JpaRepository<AppPurchase> {

	public AppPurchaseRepository() {
		super(AppPurchase.class);
	}

}

