package com.eren.assignment.sahibinden.dao;

import java.util.List;
import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.eren.assignment.sahibinden.entity.Beverage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:**/servlet-context.xml")
@Transactional
// @FixMethodOrder(MethodSorters.NAME_ASCENDING)
// @SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts
// =
// "classpath:db/insert-data.sql") })
public class BeverageDaoTest {
	@Autowired
	private BeverageDao beverageDao;

	@Test
	public void testGetAllBeverages() throws Exception {
		List<Beverage> beverages = beverageDao.getAllBeverages();
		assertThat(beverages).isNotNull().hasSize(5).extracting("name").contains("Latte").doesNotContain("Latte23");
	}

	@Test
	public void testGetBeverageWithName() throws Exception {
		Beverage beverage = beverageDao.getBeverageWithName("Latte");
		// List<Beverage> beverages = beverageDao.getAllBeverages();
		assertThat(beverage).hasFieldOrPropertyWithValue("status", "A").hasFieldOrPropertyWithValue("name", "Latte");
		// assertEquals(1, beverage.getId().longValue());
	}

}
