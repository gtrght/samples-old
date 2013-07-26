package com.othelle.samples.hibernate.dto;

import javax.persistence.*;
import java.util.List;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "DRIVER")
public class Driver {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "CAR_ID")
    private Car car;

//  Or
//  @OneToMany
//  @JoinTable(name="DRIVER_ENGINEER", joinColumns = @JoinColumn(name = "DRIVER_ID"),
//          inverseJoinColumns = @JoinColumn(name = "ENGINEER_ID"))
//  private List<Engineer> engineers;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.PERSIST)
    private List<Engineer> engineers;

    @ManyToMany(mappedBy = "drivers")
    private List<Sponsor> sponsors;

    public Driver() {
    }

    public Driver(String name) {
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Engineer> getEngineers() {
        return engineers;
    }

    public void setEngineers(List<Engineer> engineers) {
        for (Engineer engineer : engineers) {
            engineer.setDriver(this);
        }
        this.engineers = engineers;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }
}
