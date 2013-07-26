package com.othelle.samples.hibernate.dto;

import javax.persistence.*;

/**
 * author: v.vlasov
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Shape {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) //don't leave it AUTO since inheritance is used
    private int id;

    @Column
    private int side;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}
