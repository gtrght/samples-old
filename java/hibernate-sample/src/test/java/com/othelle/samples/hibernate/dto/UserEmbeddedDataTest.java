package com.othelle.samples.hibernate.dto;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.junit.Test;

import java.io.Serializable;

/**
 * author: v.vlasov
 */
public class UserEmbeddedDataTest extends HibernateTest {

    @Test
    public void testEmbedded() {
        Session session = sessionFactory.openSession();
        Address homeAddress = new Address("Somewhere", "Mountain View", "CA", "12345");
        Address workAddress = new Address("Somewhere", "Mountain View", "CA", "12343");
        UserDataEmbedded user = new UserDataEmbedded("Alex", "D.");

        UserId userId = new UserId();
        userId.setSsn("undefined_ssn");
        user.setId(userId);

        user.setHomeAddress(homeAddress);
        user.setHomeAddress(workAddress);

        Serializable id = session.save(user);
        System.out.println(new Gson().toJson(session.get(UserDataEmbedded.class, id)));
        session.close();
    }
}
