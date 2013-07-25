package com.othelle.samples.ehcache.hibernate.dao;

import com.othelle.samples.ehcache.hibernate.dto.Employee;

import java.io.Serializable;

/**
 * author: v.vlasov
 */
public interface EmployeeDao {

    Serializable save(Employee employee);

    void delete(Employee employee);

    Employee find(Integer id);

    Employee findByName(String first);
}
