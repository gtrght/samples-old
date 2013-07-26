package com.othelle.samples.hibernate.dto;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * author: v.vlasov
 */
public class UserDataCollectionsTest extends HibernateTest {

    @Test
    public void testCollections() {
        Session session = sessionFactory.openSession();

        UserDataCollections user = new UserDataCollections();

        user.setAddresses(new HashSet<Address>(Arrays.asList(new Address("Street 1", "MTV", "CA", "12345"), new Address("Street 1", "MTV", "CA", "12345"))));
        Serializable id = session.save(user);


        System.out.println(new Gson().toJson(session.get(UserDataCollections.class, id)));

        session.close();
    }
}
