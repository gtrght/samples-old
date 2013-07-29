package com.othelle.samples.hibernate.dto;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class HqlTest extends HibernateTest {

    private Function function;

    @Before
    public void setup() {
        super.setup();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < 10; i++) {
            Fish fish = new Fish();
            fish.setName("Jellyfish");
            fish.setSwimSpeed(1);
            session.save(fish);
        }

        for (int i = 0; i < 10; i++) {
            Cat cat = new Cat();
            cat.setName("Tom" + i);
            cat.setRunSpeed(i * 5);
            session.save(cat);
        }

        for (int i = 0; i < 10; i++) {
            Triangle triangle = new Triangle();
            triangle.setAngle(1.5f);
            triangle.setSide(10);
            session.save(triangle);
        }

        Human alex = new Human(89, "Alex");
        Human shonna = new Human(82, "Shonna");

        Human mike = new Human(58, "Mike");
        Human linda = new Human(45, "Linda");

        Human kirk = new Human(32, "Kirk");
        Human adam = new Human(31, "Adam");

        Human carol = new Human(29, "Carol");
        Human naomi = new Human(27, "Naomi");


        mike.getParents().add(alex);
        mike.getParents().add(shonna);

        alex.getChildren().add(mike);
        shonna.getChildren().add(mike);

        kirk.setParents(Arrays.asList(mike, linda));
        adam.setParents(Arrays.asList(mike, linda));
        carol.setParents(Arrays.asList(mike, linda));
        naomi.setParents(Arrays.asList(mike, linda));

        mike.setChildren(Arrays.asList(kirk, adam, carol, naomi));
        linda.setChildren(Arrays.asList(kirk, adam, carol, naomi));

        session.persist(alex);
        session.persist(shonna);

        transaction.commit();
        session.close();
    }

    @Test
    public void testHqlSelect() {
        Session session = sessionFactory.openSession();


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


    @Test
    public void testAdvancedConcepts() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        int updateCount = session.createQuery("update Fish set swimSpeed = :swimSpeed where id <= :id").
                setParameter("swimSpeed", 3).
                setParameter("id", 2).executeUpdate();

        assertThat(updateCount, Matchers.equalTo(2));

        transaction.commit();

        System.out.println(new Gson().toJson(session.createQuery("from Fish where id < :id").setParameter("id", 4).list()));
        System.out.println(new Gson().toJson(session.createQuery("from Cat").setMaxResults(4).list()));


        transaction = session.beginTransaction();
        int deleteCount = session.createQuery("delete from Animal where id < :id").setParameter("id", 2).executeUpdate();
        assertThat(deleteCount, Matchers.equalTo(1));


        //Polymorphism
        Number count = (Number) session.createQuery("select count(*) from Animal ").uniqueResult();
        assertThat(count.intValue(), Matchers.equalTo(19));

        //HQL Insert, no alternatives in JPQL
        session.createQuery("insert into Fish(name, swimSpeed) select c.name, 9 from Cat c").executeUpdate();
        count = (Number) session.createQuery("select count(*) from Fish ").uniqueResult();
        assertThat(count.intValue(), Matchers.equalTo(19));
        transaction.commit();


        session.close();
    }

    @Test
    public void testJoins() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        System.out.println(stringify.apply(Lists.transform(
                session.createQuery("select f.swimSpeed, t.side from Fish f, Triangle t where f.swimSpeed = t.id").list(), stringify)));

        System.out.println(stringify.apply(Lists.transform(
                session.createQuery("select dr.id, eng.id from Driver dr inner join dr.engineers eng where dr.name='Lucky Driver'").list(), stringify)));

        System.out.println(stringify.apply(Lists.transform(
                session.createQuery("select distinct dr.id, eng.id from Driver dr left join dr.engineers eng with eng.name='Engineer 1'").list(), stringify)));


        //implicit join
        System.out.println(stringify.apply(Lists.transform(
                session.createQuery("select distinct dr.id, dr.name from Driver dr where dr.sponsors.size > 1").list(), stringify)));

        //referring to collection properties
        System.out.println(stringify.apply(Lists.transform(
                session.createQuery("select distinct dr.id, dr.name from Driver dr join dr.sponsors sp where sp.id = 1").list(), stringify)));

        printResult(session.createQuery("select an.name from Animal an where type(an) = Cat ").list());


        transaction.commit();
        session.close();
    }

    @Test
    public void testPositionalParameters() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        //the funny thing there you must use setParameter(String, Object) to make this work
        printResult(session.createQuery("from Animal where id < ?1").setParameter("1", 10).setMaxResults(2).list());

        transaction.commit();

        session.close();
    }

    /**
     * COUNT (including distinct/all qualifiers) - The result type is always Long.
     * AVG - The result type is always Double.
     * MIN - The result type is the same as the argument type.
     * MAX - The result type is the same as the argument type.
     * SUM - The result type of the avg() function depends on the type of the values being averaged. For
     * integral values (other than BigInteger), the result type is Long. For floating point values (other than
     * BigDecimal) the result type is Double. For BigInteger values, the result type is BigInteger. For
     * BigDecimal values, the result type is BigDecimal.
     */
    @Test
    public void testAggregation() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        printResult(session.createQuery("select a.class, AVG(a.id) from Animal a group by a.class").list());
        transaction.commit();

        session.close();
    }

    @Test
    public void testScalarFunctions() {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        printResult(session.createQuery("select SUBSTRING(a.name, 1, 4), LENGTH(a.class), CONCAT(a.id, a.name) , " +
                "abs(a.id), SQRT(a.id), CURRENT_DATE , CURRENT_TIME , CURRENT_TIMESTAMP , UPPER(a.class) " +
                "from Cat a").list());
        transaction.commit();

        session.close();
    }

    @Test
    public void testCollectionFunctions() {
        Session session = sessionFactory.openSession();

        printResult(session.createQuery("from Human ").list());

        printResult(session.createQuery("select h.name from Human h where h.children.size > 3").list());
        printResult(session.createQuery("select parent.name, child.name from Human parent, Human child where child in elements(parent.children)").list());

        printResult(session.createQuery("select h.name, h.children[0] from Human h where h.children[0].age > 35").list());
        printResult(session.createQuery("select h.name, maxindex(h.parents) from Human h where maxindex(h.parents) < 2 group by h.name").list());

        session.close();
    }

    @Test
    public void testCaseClause() {
        Session session = sessionFactory.openSession();

        // There are some functions that worth to be look at
        // nvl( h.age, '<no age>' )
        // isnull( c.nickName, '<no nick name>' )
        // NULLIF
        // COALESCE
        printResult(session.createQuery("select h.name, h.age, case when h.age > 70 then 'retired'  else 'active' end from Human h").list());
        session.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSelectClause(){
        Session session = sessionFactory.openSession();

        //Dynamic instantiation
        List<Human> list = session.createQuery("select new Human(h.age - 20, UPPER(h.name)) from Human h").list();

        for (Human human : list) {
            session.persist(human);
        }
        printResult(session.createQuery("select id, name from Human ").list());


        // You are getting something like map(0->Alex, 1->25, 3->ALEX)
        List value = session.createQuery("select new map (h.name, h.age - 20, UPPER(h.name)) from Human h").list();
        printResult(value);

        session.close();
    }
}
