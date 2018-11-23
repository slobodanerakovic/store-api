package api.store.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import api.store.model.Store;
import api.store.util.Util;

@Repository
public class StoreRepository implements InitializingBean {
	private static final Logger LOG = LoggerFactory.getLogger(StoreRepository.class);

	/**
	 * Simulation of the repository. In Real-life app. It would read from database
	 * those informations
	 */
	private List<Store> stores = new ArrayList<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("Starting loading store data in repository...");
		String fileContent = Util.readFile();
		stores = Util.convertToStore(fileContent);

		LOG.info("Finished loading store data in repository. Repository size={}", stores.size());
	}

	public List<Store> getStores() {
		return stores;
	}

}
