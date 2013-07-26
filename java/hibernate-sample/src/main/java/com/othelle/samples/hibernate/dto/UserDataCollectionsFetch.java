package com.othelle.samples.hibernate.dto;

import javax.persistence.*;
import java.util.Collection;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "USER_DATA_COLLECTION_FETCH")
public class UserDataCollectionsFetch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "USER_ADDRESS_FETCH", joinColumns = @JoinColumn(name = "USER_ID"))
    private Collection<Address> addresses;

    public UserDataCollectionsFetch() {
    }

    public UserDataCollectionsFetch(String firstName, String lastName) {
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
