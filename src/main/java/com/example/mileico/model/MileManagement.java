package com.example.mileico.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class MileManagement {
    @Id
    @GeneratedValue
    private Long id;
    private String address = "";
    private Date startDate;
    private Date endDate;
    private double ratio;
    private int target;
    private double selled;
    private boolean isProgress;
    private boolean isFinished;
    private Long idx;
    private double percent;

    @OneToMany(mappedBy = "mileManagement")
    private List<Deposit> deposits;

    @OneToMany(mappedBy = "mileManagement")
    private List<WithDraw> withDraws;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getSelled() {
        return selled;
    }

    public void setSelled(double selled) {
        this.selled = selled;
    }

    public boolean isProgress() {
        return isProgress;
    }

    public void setProgress(boolean progress) {
        isProgress = progress;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public List<WithDraw> getWithDraws() {
        return withDraws;
    }

    public void setWithDraws(List<WithDraw> withDraws) {
        this.withDraws = withDraws;
    }

    public boolean isValidate(){
        return (target != 0 && ratio != 0 && address != null);
    }
}
