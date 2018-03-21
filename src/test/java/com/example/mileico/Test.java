package com.example.mileico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public  void main(String [] args) throws ParseException{
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        String from = "2018-03-10 10:10:10";

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date to = transFormat.parse(from);

        int i = to.compareTo(today);

        System.out.println(i);


    }
}
