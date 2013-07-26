package com.othelle.samples.hibernate.dto;

import javax.persistence.*;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "CAR")
public class Car {
    @Id
    @GeneratedValue
    private int id;


    @Column
    private String model;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public static Car create(String model) {
        Car car = new Car();
        car.model = model;
        return car;
    }

}
