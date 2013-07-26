package com.othelle.samples.hibernate.dto;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class RelationshipsTest extends HibernateTest {

    @Test
    public void testOneToOneMapping() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Driver driver = new Driver("Lucky Driver");
        String model = "BMW";
        Car car = Car.create(model);
        driver.setCar(car);
        Serializable driverId = session.save(driver);
        Serializable carId = session.save(car);

        transaction.commit();


        session.close();


        session = sessionFactory.openSession();
        Driver driverFromDb = (Driver) session.get(Driver.class, driverId);
        assertThat(driverFromDb.getCar(), Matchers.notNullValue());
        assertThat(driverFromDb.getCar().getModel(), Matchers.equalTo(model));
        assertThat(driverFromDb.getCar().getId(), Matchers.equalTo(carId));


        System.out.println(new Gson().toJson(driverFromDb));
    }

    @Test
    public void testOneToManyMapping() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Driver driver = new Driver("Lucky Driver");
        String model = "BMW";
        Car car = Car.create(model);
        driver.setCar(car);
        session.save(car);
        Serializable driverId = session.save(driver);

        driver.setEngineers(Arrays.asList(new Engineer("Engineer 1"), new Engineer("Engineer 2")));

        for (Engineer engineer : driver.getEngineers()) {
            engineer.setDriver(driver);
            session.save(engineer);
        }

        transaction.commit();

        session.close();


        session = sessionFactory.openSession();
        Driver driverFromDb = (Driver) session.get(Driver.class, driverId);
        assertThat(driverFromDb.getEngineers(), Matchers.hasSize(2));
        assertThat(driverFromDb.getEngineers().get(0), Matchers.notNullValue());
        assertThat(driverFromDb.getEngineers().get(0).getDriver(), Matchers.notNullValue());

//        System.out.println(new Gson().toJson(driverFromDb));
    }


    @Test
    public void testManyToManyMapping() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Driver driver1 = new Driver("Lucky Driver");
        Driver driver2 = new Driver("UnLucky Driver");


        Sponsor sponsor1 = new Sponsor("Lucky Team");
        Sponsor sponsor2 = new Sponsor("All stars team");

        driver1.setSponsors(Arrays.asList(sponsor1, sponsor2));
        driver2.setSponsors(Arrays.asList(sponsor1, sponsor2));

        Serializable id1 = session.save(driver1);
        Serializable id2 = session.save(driver2);

        for (Sponsor sponsor : driver1.getSponsors()) {
            sponsor.setDrivers(Arrays.asList(driver1, driver2));
            session.save(sponsor);

        }

        transaction.commit();
        session.close();

        session = sessionFactory.openSession();

        Driver driverFromDb = (Driver) session.get(Driver.class, id1);
        assertThat(driverFromDb.getSponsors(), Matchers.hasSize(2));
        assertThat(driverFromDb.getSponsors().get(0), Matchers.notNullValue());
        assertThat(driverFromDb.getSponsors().get(0).getDrivers(), Matchers.hasSize(2));

        session.close();
//        System.out.println(new Gson().toJson(driverFromDb));
    }

    @Test
    public void testCascadeSave() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Driver driver = new Driver("driver 2");
        driver.setEngineers(Arrays.asList(new Engineer("1"), new Engineer("2")));

        session.persist(driver);
        int id = driver.getId();

        transaction.commit();

        session.close();

        session = sessionFactory.openSession();

        driver = (Driver) session.get(Driver.class, 1);

        assertThat(driver.getEngineers(), Matchers.hasSize(2));
    }
}
