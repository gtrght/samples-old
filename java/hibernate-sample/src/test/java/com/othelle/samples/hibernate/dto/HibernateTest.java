package com.othelle.samples.hibernate.dto;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;

import java.util.List;

/**
 * author: v.vlasov
 */
public class HibernateTest {
    protected SessionFactory sessionFactory;

    protected static Function<Object, String> stringify = new Function<Object, String>() {
        @Override
        public String apply(Object object) {
            if (object == null) {
                return "null";
            } else if (object instanceof Iterable) {
                return String.format("(%s)", Joiner.on(", ").join((Iterable) object));
            } else if (object.getClass().isArray())
                return String.format("[%s]", Joiner.on(", ").join((Object[]) object));
            else
                return object.toString();
        }
    };

    @Before
    public void setup() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    protected void printResult(List list){
        System.out.println(stringify.apply(Lists.transform(list, stringify)));
    }
}
