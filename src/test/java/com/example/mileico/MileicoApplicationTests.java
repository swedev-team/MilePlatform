package com.example.mileico;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MileicoApplicationTests {

	@Test
	public void contextLoads() {
	}

	/*@Test
	public void getTransactionInfo() throws Exception {
		dispatcher.setConnection();
        Gson gson = new Gson();
        TransactionData data = gson.fromJson(dispatcher.response(), TransactionData.class);
		System.out.println(data.getStatus());
        System.out.println(data.getMessage());
        for(int i = 0; i< data.getResult().size(); i++) {
            System.out.println(data.getResult().get(i).getFrom());
        }
	}*/
/*
	@Test
	public void getTransactionInfo() throws Exception {
		dispatcher.setConnection("0x9063Ae372eED992e6ce4bcb21E2fF82cA14779FA");
		Gson gson = new Gson();
		TransactionData data = gson.fromJson(dispatcher.response(), TransactionData.class);
		for(int i = 0; i<data.getResult().size(); i++) {
			System.out.println(data.getResult().get(i).getTo());
		}
	}*/


}
