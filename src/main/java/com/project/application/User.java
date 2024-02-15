package com.project.application;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;


@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "firstname", nullable = false )
    private String firstname;
    @Column(name = "lastname", nullable = false )
    private String lastname;
    @Column(name = "password", nullable = false )
    private String password;
    @Pattern(regexp = "^[^@]+@[^@]+\\.com$")
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_created", nullable = false, updatable=false)
    private Date account_created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_updated", nullable = false)
    private Date account_updated;

    @PrePersist
    protected void onCreate() {
        this.account_created = new Date();
        this.account_updated = this.account_created;
    }

    public User(){

    }

    public User(String id, String firstname, String lastname, String password, String username, Date account_created, Date account_updated) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.username = username;
        this.account_created = account_created;
        this.account_updated = account_updated;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Date getAccount_created() {
        return account_created;
    }

    public Date getAccount_updated() {
        return account_updated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccount_created(Date account_created) {
        this.account_created = account_created;
    }

    public void setAccount_updated(Date account_updated) {
        this.account_updated = account_updated;
    }


}
