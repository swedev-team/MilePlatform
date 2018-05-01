package com.example.mileico.scheduler;

import com.example.mileico.etherscanDispatcher.Dispatcher;
import com.example.mileico.etherscanDispatcher.Result;
import com.example.mileico.etherscanDispatcher.TransactionData;
import com.example.mileico.model.Deposit;
import com.example.mileico.model.MileManagement;
import com.example.mileico.model.Transactions;
import com.example.mileico.model.User;
import com.example.mileico.repository.DepositRepository;
import com.example.mileico.repository.ManagementRepository;
import com.example.mileico.repository.TransactionRepository;
import com.example.mileico.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component
public class CronTable {

    @Autowired
    Dispatcher dispatcher;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ManagementRepository managementRepository;

    @Autowired
    DepositRepository depositRepository;

    //한시간에 한번씩, 0분 10분에 실행
    //@Scheduled(cron = "0 10 * * * ?")
    public void getTransactionInfo() throws IOException {
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        if(mileManagement != null) {
            if(!mileManagement.getAddress().equals("")) {
                dispatcher.setConnection(mileManagement.getAddress());
                Gson gson = new Gson();
                TransactionData data = gson.fromJson(dispatcher.response(), TransactionData.class);
                List<Transactions> transactions = transactionRepository.findAll();
                if (transactions.size() == 0) {
                    for (int i = 0; i < data.getResult().size(); i++) {
                        saveTransaction(data.getResult().get(i));
                    }
                } else {
                    for (int i = 0; i < data.getResult().size(); i++) {
                        String hash = data.getResult().get(i).getHash();
                        boolean isContain = false;
                        for (int j = 0; j < transactions.size(); j++) {
                            if (hash.equals(transactions.get(j).getHash())) {
                                isContain = true;
                                break;
                            }
                        }
                        if (!isContain) {
                            saveTransaction(data.getResult().get(i));
                        }
                    }
                }
                dispatcher.disconnect();
            }
        }
    }

    //한시간에 한번씩, 15분에 실행
    //@Scheduled(cron = "0 15 * * * ?")
    public void updateDepositInfo() {
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        if(mileManagement != null) {
            if(mileManagement.getRatio() != 0.0) {
                List<User> users = userRepository.findAll();
                List<Transactions> transactions = transactionRepository.findByIsChecked(false);
                for (int i = 0; i < transactions.size(); i++) {
                    String sender = transactions.get(i).getFrom();
                    for (int j = 0; j < users.size(); j++) {
                        User user = users.get(j);
                        if (sender.equals(user.getAddress())) {
                            Deposit deposit = new Deposit();
                            double ether = Double.parseDouble(transactions.get(i).getValue());
                            deposit.setAmount(ether);
                            deposit.setUser(user);
                            double exMile = ether * mileManagement.getRatio();
                            deposit.setExMile(exMile);
                            deposit.setDate(new Date(Calendar.getInstance().getTime().getTime()));
                            user.setFirstDeposit(true);
                            deposit.setMileManagement(mileManagement);
                            depositRepository.save(deposit);
                            mileManagement.setSelled(mileManagement.getSelled()+exMile);
                            mileManagement.setPercent(Double.parseDouble(String.format("%.2f",(mileManagement.getSelled()/mileManagement.getTarget())*100)));
                            MileManagement mile = managementRepository.save(mileManagement);
                            //목표액이 넘었을때 다음 프로젝트로 자동 이동
                            if(mile.getSelled() >= mile.getTarget()) {
                                //nextIco(mile);
                            }
                            userRepository.save(user);
                        }
                    }
                    transactions.get(i).setChecked(true);
                    transactionRepository.save(transactions);
                }
            }
            if(mileManagement.getSelled() >= mileManagement.getTarget()){
                mileManagement.setProgress(false);
                mileManagement.setFinished(true);
                managementRepository.save(mileManagement);
            }
        }

    }

    //하루에 한번씩, 하루가 끝날때
    //@Scheduled(cron = "59 59 23 * * ?")
    public void checkFinishByDate() throws IOException{
        getTransactionInfo();
        updateDepositInfo();
        MileManagement mileManagement = managementRepository.findByIsProgress(true);
        if(mileManagement != null) {
            Date endDate = mileManagement.getEndDate();
            Date today = new Date(Calendar.getInstance().getTime().getTime());
            if(today.compareTo(endDate) >= 0) {
               mileManagement.setProgress(false);
               mileManagement.setFinished(true);
               managementRepository.save(mileManagement);
            }
        }
    }
    /*@Scheduled(cron = "0/10 * * * * ?")
    public void test(){
        System.out.println("Cron test");
    }*/

    private void saveTransaction(Result result) {
        Transactions transaction = new Transactions();
        MileManagement mileManagement = managementRepository.findByIsProgress(true);

        BigDecimal bgDepositEther = new BigDecimal(result.getValue());
        BigDecimal forDivide = new BigDecimal("1000000000000000000");
        BigDecimal depositEther = bgDepositEther.divide(forDivide);
        transaction.setFrom(result.getFrom().toLowerCase());
        transaction.setBlockHash(result.getBlockHash());
        transaction.setBlockNumber(result.getBlockNumber());
        transaction.setConfirmations(result.getConfirmations());
        transaction.setCumulativeGasUsed(result.getCumulativeGasUsed());
        transaction.setGas(result.getGas());
        transaction.setGasPrice(result.getGasPrice());
        transaction.setGasUsed(result.getGasUsed());
        transaction.setHash(result.getHash());
        transaction.setIsError(result.getIsError());
        transaction.setNonce(result.getNonce());
        transaction.setTimeStamp(result.getTimeStamp());
        transaction.setTo(result.getTo().toLowerCase());
        transaction.setTransactionIndex(result.getTransactionIndex());
        transaction.setTxreceipt_status(result.getTxreceipt_status());
        transaction.setValue(depositEther.toString());
        transaction.setDepositDate();
        transaction.setExMile(String.valueOf((depositEther.doubleValue() * mileManagement.getRatio())));
        transactionRepository.save(transaction);
    }


   /* private void nextIco(MileManagement mileManagement) {
        mileManagement.setProgress(false);
        mileManagement.setFinished(true);
        MileManagement next = managementRepository.findByIdx(mileManagement.getIdx()+1);
        managementRepository.save(mileManagement);
        if(next != null) {
            next.setProgress(true);
            managementRepository.save(next);
        }
    }*/
}
