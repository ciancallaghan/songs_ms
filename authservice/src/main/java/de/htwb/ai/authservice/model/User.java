package de.htwb.ai.authservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usertable")
public class User {
    @Id
    private String userid;
    private String password;
    private String firstname;
    private String lastname;

    public User() {
    }

    public User(String userid, String password, String firstname, String lastname) {
        this.userid = userid;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}