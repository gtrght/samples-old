package com.othelle.samples.hibernate.dto;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collection;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "USER_DATA_COLLECTION")
public class UserDataCollections {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ElementCollection
    @JoinTable(name = "USER_ADDRESS", joinColumns = @JoinColumn(name = "USER_ID"))
    @GenericGenerator(name = "hilo-gen", strategy = "hilo")
    @CollectionId(columns = {@Column(name="ADDRESS_ID")}, generator = "hilo-gen", type = @Type(type = "int"))
    private Collection<Address> addresses;

    public UserDataCollections() {
    }

    public UserDataCollections(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
