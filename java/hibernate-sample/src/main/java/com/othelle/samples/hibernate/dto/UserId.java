package com.othelle.samples.hibernate.dto;

import javax.persistence.*;
import java.io.Serializable;

/**
 * author: v.vlasov
 */
@Embeddable
public class UserId implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //@Id Never use in Embedded classes

    @Column(name = "ssn")
    private String ssn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
