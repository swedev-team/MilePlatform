package com.example.mileico.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity

public class WithDraw {

    @Id
    @GeneratedValue
    private Long Id;

    private Date date;

    private double amount;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_withdraw_user"))
    private User user;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_withdraw_management"))
    private MileManagement mileManagement;

    public void setId(Long id) {
        Id = id;
    }

    public void setMileManagement(MileManagement mileManagement) {
        this.mileManagement = mileManagement;
    }

    public MileManagement getMileManagement() {
        return mileManagement;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return Id;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

}
