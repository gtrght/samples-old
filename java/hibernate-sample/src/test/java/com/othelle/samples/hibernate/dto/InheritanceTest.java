package com.othelle.samples.hibernate.dto;

import org.hibernate.Session;
import org.junit.Test;

import java.io.Serializable;

/**
 * author: v.vlasov
 */
public class InheritanceTest extends HibernateTest {
    @Test
    public void testInheritance() {
        Session session = sessionFactory.openSession();

        Cat cat = new Cat();
        Fish fish = new Fish();

        session.save(cat);
        session.save(fish);

        session.close();


        session = sessionFactory.openSession();

        cat = (Cat) session.get(Cat.class, 1);
        fish = (Fish) session.get(Fish.class, 2);
    }

    @Test
    public void testInheritanceDifferentTables() {
        Session session = sessionFactory.openSession();

        Triangle triangle = new Triangle();
        Rectangle rectangle = new Rectangle();

        Serializable triangleId = session.save(triangle);
        Serializable rectangleId = session.save(rectangle);

        session.close();
        session = sessionFactory.openSession();

        session.get(Triangle.class, triangleId);
        session.get(Rectangle.class, rectangleId);
    }
}
