package com.othelle.samples.ehcache.hibernate.dao.impl;

import com.othelle.samples.ehcache.hibernate.dto.Employee;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class HibernateEmployeeDaoTest {
    private HibernateEmployeeDao employeeDao;
    private SessionFactory sessionFactory;

    @Before
    public void setup() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        employeeDao = new HibernateEmployeeDao();
        employeeDao.setSession(sessionFactory.openSession());
    }


    @Test
    public void testHibernateFirstLevelCache() {
        Integer id1 = employeeDao.save(new Employee("Alex", "Smith"));
        Integer id2 = employeeDao.save(new Employee("Tim", "Ray"));


        Session session = sessionFactory.openSession();

        employeeDao.setSession(session); //the same session for all the iterations
        for (int i = 0; i < 10; i++) {
            employeeDao.find(id1);
            employeeDao.find(id2);

        }
        session.close(); //it's not required to close the session to make second level cache work

        System.out.println(String.format("First Level (Session) Cache: Hits: %s, Misses: %d", "n/a",
                sessionFactory.getStatistics().getEntityLoadCount()));

        //We only fetch entries 1 time per object
        assertThat(sessionFactory.getStatistics().getEntityLoadCount(), Matchers.equalTo(2l));
        assertThat(sessionFactory.getStatistics().getEntityInsertCount(), Matchers.equalTo(2l));
    }

    @Test
    public void testHibernateSecondLevelCache() {
        Integer id1 = employeeDao.save(new Employee("Alex", "Smith"));
        Integer id2 = employeeDao.save(new Employee("Tim", "Ray"));

        System.out.println(employeeDao.find(id1));
        System.out.println(employeeDao.find(id2));


        //demonstrating
        for (int i = 0; i < 10; i++) {
            Session session = sessionFactory.openSession();
            employeeDao.setSession(session);
            employeeDao.find(id1);
            employeeDao.find(id2);

            session.close(); //it's not required to close the session to make second level cache work
        }


        System.out.println(String.format("Second Level Cache. Hits: %d, Misses: %d", sessionFactory.getStatistics().getSecondLevelCacheHitCount(),
                sessionFactory.getStatistics().getSecondLevelCacheMissCount()));

        assertThat(sessionFactory.getStatistics().getSecondLevelCacheHitCount(), Matchers.greaterThan(0l));
        assertThat(sessionFactory.getStatistics().getSecondLevelCacheMissCount(), Matchers.equalTo(2l));
    }

    @Test
    public void testHibernateQueryLevelCache() {
        employeeDao.save(new Employee("Alex", "Smith"));
        employeeDao.save(new Employee("Tim", "Ray"));

        System.out.println(employeeDao.findByName("Alex"));
        System.out.println(employeeDao.findByName("Tim"));
        employeeDao.findByName("Alex");
        employeeDao.findByName("Tim");


        for (int i = 0; i < 10; i++) {
            Session session = sessionFactory.openSession();
            employeeDao.setSession(session);

            employeeDao.findByName("Alex");
            employeeDao.findByName("Tim");
            session.close(); //it's not required to close the session to make second level cache work
        }


        System.out.println(String.format("Query Cache. Hits: %d, Misses: %d",
                sessionFactory.getStatistics().getQueryCacheHitCount(),
                sessionFactory.getStatistics().getQueryCacheMissCount()));

        System.out.println(sessionFactory.getStatistics().getSecondLevelCacheHitCount());

        assertThat(sessionFactory.getStatistics().getQueryCacheHitCount(), Matchers.greaterThan(0l));
        assertThat(sessionFactory.getStatistics().getQueryCacheMissCount(), Matchers.equalTo(2l));
    }
}
