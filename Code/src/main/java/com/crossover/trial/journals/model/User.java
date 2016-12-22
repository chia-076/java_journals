package com.crossover.trial.journals.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String loginName;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Subscription> subscriptions;

    @Column(name = "email", nullable = false)
    private String email;

    public User() {
    }

    public User(Long id, String loginName, String pwd, Boolean enabled, Role role, List<Subscription> subscriptions) {
        this.id = id;
        this.loginName = loginName;
        this.pwd = pwd;
        this.enabled = enabled;
        this.role = role;
        this.subscriptions = subscriptions;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
