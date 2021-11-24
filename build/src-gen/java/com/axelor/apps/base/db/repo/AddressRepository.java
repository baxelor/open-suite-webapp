package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.Address;
import com.axelor.db.JpaRepository;

public class AddressRepository extends JpaRepository<Address> {

	public AddressRepository() {
		super(Address.class);
	}

}

