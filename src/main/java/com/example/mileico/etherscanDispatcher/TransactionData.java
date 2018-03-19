package com.example.mileico.etherscanDispatcher;

import java.util.List;

public class TransactionData {
    String status;
    String message;
    List<Result> result;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Result> getResult() {
        return result;
    }
}
