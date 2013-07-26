package com.othelle.samples.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * author: v.vlasov
 */
@Entity
@DiscriminatorValue("Fish")
public class Fish extends Animal {
    @Column(name = "swim_speed")
    private int swimSpeed;

    public int getSwimSpeed() {
        return swimSpeed;
    }

    public void setSwimSpeed(int swimSpeed) {
        this.swimSpeed = swimSpeed;
    }


}
