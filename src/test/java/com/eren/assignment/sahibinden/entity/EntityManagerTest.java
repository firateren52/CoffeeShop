package com.eren.assignment.sahibinden.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:**/servlet-context.xml")
@Transactional
public class EntityManagerTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	//@Ignore
	public void shouldHaveAnEntityManager() {
		assertNotNull(entityManager);
	}

	@Test
	@Ignore
	public void shouldHaveNoObjectsAtStart() {
		List<?> results = entityManager.createQuery("from Beverage").getResultList();
		assertTrue(results.isEmpty());
	}

	@Test
	//@Ignore
	public void shouldBeAbleToPersistAnObject() {
		int beverageCount = entityManager.createQuery("from Beverage").getResultList().size();
		Beverage jobUser = new Beverage();
		jobUser.setCost(5);
		jobUser.setName("tea");
		jobUser.setCdate(new Date());
		entityManager.persist(jobUser);
		entityManager.flush();
		assertEquals(beverageCount + 1, entityManager.createQuery("from Beverage").getResultList().size());
	}

	@Test
	//@Ignore
	public void shouldBeABleToQueryForObjects() {
		shouldBeAbleToPersistAnObject();

		assertEquals(1, entityManager.createQuery("from Beverage where name = 'tea'").getResultList().size());
		assertEquals(0, entityManager.createQuery("from Beverage where name = 'Baz'").getResultList().size());
	}

}
