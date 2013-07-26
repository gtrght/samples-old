package com.othelle.samples.hibernate.dto;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class CrudTest extends HibernateTest {

    @Test
    public void testCreate() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 10; i++) {
            Fish fish = new Fish();
            fish.setSwimSpeed(i + 1);
            fish.setName("Fish " + i);
            session.save(fish);
        }

        transaction.commit();

        Animal fish = (Animal) session.get(Fish.class, 2);
        fish.setName("My personal fish");
        session.save(fish);


        fish = (Animal) session.get(Fish.class, 2);
        System.out.println(new Gson().toJson(fish));
        session.close();
    }

    @Test
    public void testTransientPersistentDetached() {
        Session session = sessionFactory.openSession();

        Cat cat = new Cat();
        cat.setName("test");
        Transaction transaction = session.beginTransaction();

        Serializable id = session.save(cat);
        cat.setName("test1");
        cat.setName("test2");

        transaction.commit();

        cat.setName("test3");

        transaction = session.beginTransaction();
        Cat catty = (Cat) session.get(Cat.class, id);
        assertThat(catty.getName(), Matchers.equalTo("test3"));



        catty.setName("New Name?");
        transaction.commit();
        assertThat(((Cat) session.get(Cat.class, id)).getName(), Matchers.equalTo("New Name?"));
    }
}
