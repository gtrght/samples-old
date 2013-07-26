package com.othelle.samples.hibernate.dto;


import org.hamcrest.Matchers;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class UserDataCollectionsFetchTest extends HibernateTest {
    @Test
    public void testFetching() throws NoSuchFieldException, IllegalAccessException {
        Session session = sessionFactory.openSession();
        UserDataCollectionsFetch user = new UserDataCollectionsFetch("Vasily", "V.");
        Transaction transaction = session.beginTransaction();

        user.setAddresses(new HashSet<Address>(Arrays.asList(new Address("Street 1", "MTV", "CA", "12345"), new Address("Street 1", "MTV", "CA", "12345"))));
        Serializable id = session.save(user);

        transaction.commit();
        session.close();

        session = sessionFactory.openSession();

        user = (UserDataCollectionsFetch) session.get(UserDataCollectionsFetch.class, id);

        assertThat(user.getAddresses().size(), Matchers.equalTo(2));
        session.close();
    }


    @Test(expected = LazyInitializationException.class)
    public void testFetchingLazySessionClosed() throws NoSuchFieldException, IllegalAccessException {
        Session session = sessionFactory.openSession();
        UserDataCollectionsFetch user = new UserDataCollectionsFetch("Vasily", "V.");
        Transaction transaction = session.beginTransaction();

        user.setAddresses(new HashSet<Address>(Arrays.asList(new Address("Street 1", "MTV", "CA", "12345"), new Address("Street 1", "MTV", "CA", "12345"))));
        Serializable id = session.save(user);

        transaction.commit();
        session.close();

        session = sessionFactory.openSession();

        user = (UserDataCollectionsFetch) session.get(UserDataCollectionsFetch.class, id);
        session.close();
        assertThat(user.getAddresses().size(), Matchers.equalTo(2));
    }

}
