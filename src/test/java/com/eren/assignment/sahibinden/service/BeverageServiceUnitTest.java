package com.eren.assignment.sahibinden.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.eren.assignment.sahibinden.dao.BeverageDao;
import com.eren.assignment.sahibinden.entity.Beverage;
import com.eren.assignment.sahibinden.entity.enums.HISTORICAL_ENTITY_STATUS;
import com.eren.assignment.sahibinden.exception.ServiceRuntimeException;
import com.eren.assignment.sahibinden.service.impl.BeverageServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BeverageServiceUnitTest {
	@InjectMocks
	private BeverageServiceImpl beverageService;

	@Mock
	private BeverageDao beverageDao;

	Beverage existingBeverage;

	@Before
	public void setup() {
		existingBeverage = new Beverage();
		existingBeverage.setId(3l);
		existingBeverage.setCost(5);
		existingBeverage.setStatus(HISTORICAL_ENTITY_STATUS.ACTIVE.getCode());
		existingBeverage.setName("Mocha");
		existingBeverage.setCdate(new Date());
	}

	@Test(expected = ServiceRuntimeException.class)
	public void testCreateNewBeverageException() throws ServiceRuntimeException {
		when(beverageDao.getBeverageWithName(existingBeverage.getName())).thenReturn(existingBeverage);

		Beverage beverage = new Beverage();
		beverage.setCost(5);
		beverage.setName(existingBeverage.getName());
		beverage.setCdate(new Date());

		try {
			beverageService.createNewBeverage(beverage);
		} catch (ServiceRuntimeException ex) {
			verify(beverageDao, times(0)).save(beverage);
			throw ex;
		}
	}

	@Test
	public void testCreateNewBeverage() throws ServiceRuntimeException {
		String name = "Mocha2";
		when(beverageDao.getBeverageWithName(name)).thenReturn(null);

		Beverage beverage = new Beverage();
		beverage.setCost(5);
		beverage.setName(name);
		beverage.setCdate(new Date());

		beverageService.createNewBeverage(beverage);
		verify(beverageDao, times(1)).save(beverage);
		assertThat(beverage).hasFieldOrPropertyWithValue("status", HISTORICAL_ENTITY_STATUS.ACTIVE.getCode());
	}

	@Test
	public void testUpdateBeverage() throws Exception {
		when(beverageService.getBeverage(3)).thenReturn(existingBeverage);
		Beverage beverage = beverageService.getBeverage(3);
		beverage.setCost(10);
		beverageService.updateBeverage(beverage);
		Beverage beverage2 = beverageService.getBeverage(3);

		verify(beverageDao, times(1)).save(beverage);
		verify(beverageDao, times(1)).update(beverage);

		assertThat(beverage2).hasFieldOrPropertyWithValue("cost", 10).hasFieldOrPropertyWithValue("status",
				HISTORICAL_ENTITY_STATUS.DEACTIVE.getCode());
	}

	@Test
	public void testGetAllBeverages() throws Exception {
		List<Beverage> list = new ArrayList<Beverage>();
		list.add(existingBeverage);

		when(beverageDao.getAllBeverages()).thenReturn(list);
		List<Beverage> beverages = beverageService.getAllBeverages();
		assertThat(beverages).isNotNull().hasSize(1).extracting("name").contains(existingBeverage.getName())
				.doesNotContain("Latte23");
	}

	@Test
	public void testGetBeverage() throws Exception {
		when(beverageDao.findById(existingBeverage.getId())).thenReturn(existingBeverage);
		Beverage beverage = beverageService.getBeverage(existingBeverage.getId());

		verify(beverageDao, times(1)).findById(existingBeverage.getId());
		assertThat(beverage).hasFieldOrPropertyWithValue("status", "A").hasFieldOrPropertyWithValue("name",
				existingBeverage.getName());
	}

}
