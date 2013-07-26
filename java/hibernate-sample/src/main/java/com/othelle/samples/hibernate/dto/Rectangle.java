package com.othelle.samples.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * author: v.vlasov
 */
@Entity
public class Rectangle extends Shape {
    @Column
    private boolean square;

    public boolean isSquare() {
        return square;
    }

    public void setSquare(boolean square) {
        this.square = square;
    }
}
