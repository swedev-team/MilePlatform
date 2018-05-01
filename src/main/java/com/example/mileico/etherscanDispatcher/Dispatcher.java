package com.example.mileico.etherscanDispatcher;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class Dispatcher {

    private final String API_KEY = "MBXYIJ56YWU9N6VAQ5JTB4TV6ISUV5I2K5";
    private final String API_KEY2 = "KSGN2PMF492WZTPAUPT9RTM5CZ7D5UYBBF";
    private final String URL = "http://api.etherscan.io/api?module=account&action=txlist";
    private final String URL2 = "http://api-ropsten.etherscan.io/api?module=account&action=txlist";

    HttpURLConnection conn;

    public void setConnection(String adminAddr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) (new URL(URL+"&address="+adminAddr+"&startblock=0&endblock=99999999&sort=asc&apikey="+API_KEY).openConnection());
        conn.setRequestProperty("X-Requested-With", "Curl");
        //conn.setRequestProperty("Authorization", );
        conn.setDoOutput(true);
        this.conn = conn;
    }

    public String response() throws IOException {
        InputStreamReader is = new InputStreamReader(conn.getInputStream());
        BufferedReader rd = new BufferedReader(is);
        String result = rd.readLine();
        rd.close();
        return result;
    }

    public void disconnect(){
        conn.disconnect();;
    }

}
