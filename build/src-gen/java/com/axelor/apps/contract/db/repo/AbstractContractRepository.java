package com.axelor.apps.contract.db.repo;

import com.axelor.apps.contract.db.Contract;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public abstract class AbstractContractRepository extends JpaRepository<Contract> {

	public AbstractContractRepository() {
		super(Contract.class);
	}

	public Contract findByName(String name) {
		return Query.of(Contract.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	static final String CUSTOMER_CONTRACT_SEQUENCE = "customerContract";
	static final String SUPPLIER_CONTRACT_SEQUENCE = "supplierContract";

	public static final int DRAFT_CONTRACT = 1;
	public static final int ACTIVE_CONTRACT = 2;
	public static final int CLOSED_CONTRACT = 3;

	public static final int CUSTOMER_CONTRACT = 1;
	public static final int SUPPLIER_CONTRACT = 2;
}

