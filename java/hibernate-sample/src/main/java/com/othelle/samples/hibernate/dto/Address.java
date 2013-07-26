package com.othelle.samples.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * author: v.vlasov
 */
@Embeddable
public class Address {
    @Column(name = "zip_code")
    private String zip;

    @Column(name = "state_name")
    private String state;

    @Column(name = "city_name")
    private String city;

    @Column(name = "street_name")
    private String street;

    public Address() {
    }

    public Address(String address, String city, String state, String zip) {
        this.street = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
