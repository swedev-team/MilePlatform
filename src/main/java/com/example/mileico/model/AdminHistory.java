package com.example.mileico.model;


import javax.persistence.*;
import java.sql.Date;

@Entity
public class AdminHistory {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String action;

    private Date date;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAction() {
        return action;
    }

    public Date getDate() {
        return date;
    }
}
