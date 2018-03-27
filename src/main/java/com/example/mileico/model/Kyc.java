package com.example.mileico.model;


import com.mysql.jdbc.Blob;
import lombok.Data;

import javax.persistence.*;


@Entity
public class Kyc {

    @Id
    @GeneratedValue
    private Long id;

    private String first;

    private String second;

    private String third;

    @Column(columnDefinition = "longblob")
    private byte[] passport;

    @Column(columnDefinition = "longblob")
    private byte[] selfPicture;

    private String email;

    private String firstName;

    private String lastName;

    private String nation;

    private String live;

    private String day;

    private String month;

    private String year;

    private String gender;

    @OneToOne(mappedBy = "kyc")
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public void setPassport(byte[] passport) {
        this.passport = passport;
    }

    public void setSelfPicture(byte[] selfPicture) {
        this.selfPicture = selfPicture;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }

    public byte[] getPassport() {
        return passport;
    }

    public byte[] getSelfPicture() {
        return selfPicture;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNation() {
        return nation;
    }

    public String getLive() {
        return live;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "Kyc{" +
                "id=" + id +
                ", first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", third='" + third + '\'' +
                ", passport='" + passport + '\'' +
                ", selfPicture='" + selfPicture + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nation='" + nation + '\'' +
                ", live='" + live + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", gender='" + gender + '\'' +
                ", user=" + user +
                '}';
    }
}
