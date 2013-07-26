package com.othelle.samples.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * author: v.vlasov
 */
@Entity
@DiscriminatorValue("Cat")
@org.hibernate.annotations.Entity(selectBeforeUpdate = true) //update only when something is changed
public class Cat extends Animal {
    @Column(name = "run_speed")
    private int runSpeed;

    public int getRunSpeed() {
        return runSpeed;
    }

    public void setRunSpeed(int runSpeed) {
        this.runSpeed = runSpeed;
    }
}
