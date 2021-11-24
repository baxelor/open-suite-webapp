package com.axelor.apps.sale.db.repo;

import com.axelor.apps.sale.db.ConfiguratorCreator;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class ConfiguratorCreatorRepository extends JpaRepository<ConfiguratorCreator> {

	public ConfiguratorCreatorRepository() {
		super(ConfiguratorCreator.class);
	}

	public ConfiguratorCreator findByName(String name) {
		return Query.of(ConfiguratorCreator.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

