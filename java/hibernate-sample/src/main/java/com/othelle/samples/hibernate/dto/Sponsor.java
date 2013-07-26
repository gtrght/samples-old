package com.othelle.samples.hibernate.dto;

import javax.persistence.*;
import java.util.List;

/**
 * author: v.vlasov
 */
@Entity
public class Sponsor {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "company_name")
    private String companyName;

    @ManyToMany
    @JoinTable(name = "SPONSOR_DRIVER",
            joinColumns = @JoinColumn(name = "SPONSOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "DRIVER_ID"))
    private List<Driver> drivers;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sponsor(String companyName) {
        this.companyName = companyName;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Sponsor() {
    }
}
