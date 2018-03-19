package com.example.mileico.model;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    //kyc 승인 여부
    private boolean isChecked = false;

    //제출여부
    private boolean isSubmitted = false;

    private String address = "";

    private boolean isAdmin = false;

    private boolean isFirstDeposit = false;

    private String kycStatus = "미확인";

    private String failReason = "";

    private String phoneNumber;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_kyc"))
    private Kyc kyc;

    @OneToMany(mappedBy = "user")
    private List<Deposit> deposits;

    @OneToMany(mappedBy = "user")
    private List<WithDraw> withDraws;

    @Basic(optional = false)
    @Column(name = "joinDate", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;

    @Column(length = 1 , nullable = false , columnDefinition = "default=n")
    private String withdraw = "n";

    @Basic(optional = false)
    @Column(name = "withdrawDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date withdrawDate;

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public void setWithDraws(List<WithDraw> withDraws) {
        this.withDraws = withDraws;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public List<WithDraw> getWithDraws() {
        return withDraws;
    }

    public void setFirstDeposit(boolean firstDeposit) {
        isFirstDeposit = firstDeposit;
    }

    public boolean isFirstDeposit() {
        return isFirstDeposit;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKyc(Kyc kyc) {
        this.kyc = kyc;
    }

    public Kyc getKyc() {
        return kyc;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setAddress(String address) {
        this.address = address.toLowerCase();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getAddress() {
        return address;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public Date getWithdrawDate() {
        return withdrawDate;
    }

    public void setWithdrawDate(Date withdrawDate) {
        this.withdrawDate = withdrawDate;
    }

    public boolean isWithdraw(){
        if(this.withdraw.equals("n"))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isChecked=" + isChecked +
                ", isSubmitted=" + isSubmitted +
                ", address='" + address + '\'' +
                ", isAdmin=" + isAdmin +
                ", isFirstDeposit=" + isFirstDeposit +
                ", kycStatus='" + kycStatus + '\'' +
                ", failReason='" + failReason + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", kyc=" + kyc +
                ", deposits=" + deposits +
                ", withDraws=" + withDraws +
                ", joinDate=" + joinDate +
                ", withdraw='" + withdraw + '\'' +
                ", withdrawDate=" + withdrawDate +
                '}';
    }
}
