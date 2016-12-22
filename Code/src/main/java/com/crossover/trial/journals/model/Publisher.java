package com.crossover.trial.journals.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Publisher {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String name;

    public Publisher() {
    }

    public Publisher(Long id, User user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
