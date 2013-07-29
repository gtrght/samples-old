package com.othelle.samples.hibernate.dto;

import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author: v.vlasov
 */
@Entity
public class Human {
    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int age;


    //@IndexColumn to make it possible to use: select h.name, h.children[0] from Human h where h.children[0].age > 357
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "HUMAN_CHILDREN")
    @IndexColumn(name = "child_id")
    private List<Human> children = new ArrayList<Human>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "HUMAN_PARENTS")
    @IndexColumn(name = "parent_id")
    private List<Human> parents = new ArrayList<Human>();


    public List<Human> getChildren() {
        return children;
    }

    public void setChildren(List<Human> children) {
        this.children = children;
    }

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

    public List<Human> getParents() {
        return parents;
    }

    public void setParents(List<Human> parents) {
        this.parents = parents;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Human(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Human() {
    }
}
