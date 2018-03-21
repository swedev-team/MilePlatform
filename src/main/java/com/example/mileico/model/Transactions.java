package com.example.mileico.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Transactions {

    @Id
    @GeneratedValue
    private Long id;

    private String blockNumber="";

    private String timeStamp="";

    private String hash="";

    private String nonce="";

    private String blockHash="";

    private String transactionIndex="";

    private String sender="";

    private String receiver="";

    private String value="";

    private String gas="";

    private String gasPrice="";

    private String isError="";

    private String txreceipt_status="";

    private String cumulativeGasUsed="";

    private String gasUsed="";

    private String confirmations="";

    private Boolean isChecked = false;

    private String depositDate="";

    private String exMile="";

    public void setDepositDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
        Date date = new Date();
        this.depositDate = simpleDateFormat.format(date);
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setExMile(String exMile) {
        this.exMile = exMile;
    }

    public String getExMile() {
        return exMile;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public void setFrom(String from) {
        this.sender = from;
    }

    public void setTo(String to) {
        this.receiver = to;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }

    public void setTxreceipt_status(String txreceipt_status) {
        this.txreceipt_status = txreceipt_status;
    }

    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public void setConfirmations(String confirmations) {
        this.confirmations = confirmations;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Long getId() {
        return id;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public String getNonce() {
        return nonce;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public String getFrom() {
        return sender;
    }

    public String getTo() {
        return receiver;
    }

    public String getValue() {
        return value;
    }

    public String getGas() {
        return gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public String getIsError() {
        return isError;
    }

    public String getTxreceipt_status() {
        return txreceipt_status;
    }

    public String getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public String getConfirmations() {
        return confirmations;
    }

    public Boolean getChecked() {
        return isChecked;
    }
}
