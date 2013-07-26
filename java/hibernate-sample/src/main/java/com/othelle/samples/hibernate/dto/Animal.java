package com.othelle.samples.hibernate.dto;

import javax.persistence.*;

/**
 * author: v.vlasov
 */
@Entity
@Table(name = "ANIMAL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "ANIMAL_FAMILY",
        discriminatorType = DiscriminatorType.STRING
)
@NamedQuery(name = "Animal.By.Id", query = "select id from Animal where id > :id")
@NamedNativeQuery(name = "Native.Find.By.Name", query = "select * from ANIMAL where name = ?", resultClass = Animal.class)
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
