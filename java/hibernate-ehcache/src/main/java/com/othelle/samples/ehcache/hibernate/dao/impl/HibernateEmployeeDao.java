package com.othelle.samples.ehcache.hibernate.dao.impl;

import com.othelle.samples.ehcache.hibernate.dao.EmployeeDao;
import com.othelle.samples.ehcache.hibernate.dto.Employee;
import org.hibernate.Session;

/**
 * author: v.vlasov
 */
public class HibernateEmployeeDao implements EmployeeDao {
    private Session session;

    @Override
    public Integer save(Employee employee) {
        return (Integer) session.save(employee);
    }

    @Override
    public void delete(Employee employee) {
        session.delete(employee);
    }

    @Override
    public Employee find(Integer id) {
        return (Employee) session.get(Employee.class, id);
    }

    @Override
    public Employee findByName(String first) {
        return (Employee) session.createQuery("from Employee employee where employee.firstName=?").setParameter(0, first)
                .setCacheable(true).uniqueResult();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
