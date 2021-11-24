package com.axelor.apps.base.db.repo;

import com.axelor.apps.base.db.Country;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class CountryRepository extends JpaRepository<Country> {

	public CountryRepository() {
		super(Country.class);
	}

	public Country findByName(String name) {
		return Query.of(Country.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

