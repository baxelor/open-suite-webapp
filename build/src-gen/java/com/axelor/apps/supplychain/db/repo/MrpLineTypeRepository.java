package com.axelor.apps.supplychain.db.repo;

import com.axelor.apps.supplychain.db.MrpLineType;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class MrpLineTypeRepository extends JpaRepository<MrpLineType> {

	public MrpLineTypeRepository() {
		super(MrpLineType.class);
	}

	public MrpLineType findByCode(String code) {
		return Query.of(MrpLineType.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public MrpLineType findByName(String name) {
		return Query.of(MrpLineType.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// ELEMENT SELECT
	public static final int ELEMENT_AVAILABLE_STOCK = 1;  // Available stock
	public static final int ELEMENT_PURCHASE_ORDER = 2;  // Purchase order
	public static final int ELEMENT_SALE_ORDER = 3;  // Sale order
	public static final int ELEMENT_SALE_FORECAST = 4;  // Sales forecast

	public static final int ELEMENT_PURCHASE_PROPOSAL = 5;  // Purchase proposal

	public static final int ELEMENT_MANUFACTURING_ORDER = 6;  // Manufacturing order
	public static final int ELEMENT_MANUFACTURING_ORDER_NEED = 7;  // Need manufacturing order

	public static final int ELEMENT_MANUFACTURING_PROPOSAL = 8;  // Manufacturing proposal
	public static final int ELEMENT_MANUFACTURING_PROPOSAL_NEED = 9;  // Need manufacturing proposal

	// TYPE SELECT
	public static final int TYPE_IN = 1;  // Entry of stock
	public static final int TYPE_OUT = 2;  // Out of stock

	// APPLICATION FIELD SELECT
	public static final int APPLICATION_FIELD_MRP = 1;
	public static final int APPLICATION_FIELD_MPS = 2;

	// ELEMENT SELECT
	public static final int ELEMENT_MASTER_PRODUCTION_SCHEDULING = 10;  // Master Production Scheduling
}

