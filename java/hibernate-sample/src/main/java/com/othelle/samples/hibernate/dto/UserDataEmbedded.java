package com.othelle.samples.hibernate.dto;

import javax.persistence.*;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "USER_DATA_EMBEDDED")
public class UserDataEmbedded {

    @EmbeddedId
    private UserId id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @AttributeOverrides(value = {
            @AttributeOverride(name = "city", column = @Column(name = "work_city_name")),
            @AttributeOverride(name = "zip", column = @Column(name = "work_zip_name")),
            @AttributeOverride(name = "state", column = @Column(name = "work_state_name")),
            @AttributeOverride(name = "street", column = @Column(name = "work_street_name"))
    })
    private Address workAddress;

    private Address homeAddress;

    public UserDataEmbedded() {
    }

    public UserDataEmbedded(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address address) {
        this.homeAddress = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }
}
