package com.othelle.samples.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * author: v.vlasov
 */
@Entity
public class Triangle extends Shape {
    @Column
    private float angle;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
