package com.othelle.samples.hibernate.dto;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

/**
 * author: v.vlasov
 */
public class HqlTest extends HibernateTest {

    @Test
    public void testHqlSelect() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 10; i++) {
            Fish fish = new Fish();
            fish.setName("Jellyfish");
            fish.setSwimSpeed(1);
            session.save(fish);
        }
        transaction.commit();

        Query query = session.createQuery("from Animal").setFirstResult(3).setMaxResults(3);

        List animals = query.list();

        for (Object animal : animals)
            System.out.println(new Gson().toJson(animal));

        Query names = session.createQuery("select name from Animal").setFirstResult(3).setMaxResults(4);
        System.out.println(Joiner.on("\n").join(names.list()));


        names = session.createQuery("select id, name from Animal where id > :id").setInteger("id", 2)
                .setFirstResult(3).setMaxResults(4);
        List list = names.list();
        System.out.println(Joiner.on("\n").join(list));


        //named queries
        list = session.getNamedQuery("Animal.By.Id").setParameter("id", 3).list();
        System.out.println(Joiner.on("\n").join(list));

        //native named query
        list = session.getNamedQuery("Native.Find.By.Name").setParameter(0, "Jellyfish").setFirstResult(5).setMaxResults(2).list();
        System.out.println(Joiner.on("\n").join(list));

        session.close();
    }


}
