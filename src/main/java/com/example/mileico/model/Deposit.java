package com.example.mileico.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Deposit {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_deposit_user"))
    private User user;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_deposit_management"))
    private MileManagement mileManagement;
    private Date date;
    private double amount;
    private double exMile;
    private boolean isReturned;

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMileManagement(MileManagement mileManagement) {
        this.mileManagement = mileManagement;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setExMile(double exMile) {
        this.exMile = exMile;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public MileManagement getMileManagement() {
        return mileManagement;
    }

    public double getAmount() {
        return amount;
    }

    public double getExMile() {
        return exMile;
    }

    public boolean isReturned() {
        return isReturned;
    }


}
