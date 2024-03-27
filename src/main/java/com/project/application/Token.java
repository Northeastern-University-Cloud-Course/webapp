package com.project.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;

@Entity
public class Token {

    @Id
    @Column(name = "email", nullable = false )
    private String email;
    @Column(name = "link", nullable = false )
    private String link;
    @Column(name = "exptime", nullable = false )
    private Timestamp exptime;
    @Column(name = "verified", nullable = false )
    private boolean verified=false;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getExptime() {
        return exptime;
    }

    public void setExptime(Timestamp exptime) {
        this.exptime = exptime;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
