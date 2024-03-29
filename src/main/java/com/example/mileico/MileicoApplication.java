package com.example.mileico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;


@SpringBootApplication
@EnableScheduling
public class MileicoApplication {


	public static void main(String[] args) {
		SpringApplication.run(MileicoApplication.class, args);
	}


	//스케쥴러
	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}



/*
	@Bean
	public TaskScheduler ThreadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);

		return threadPoolTaskScheduler;
}*/
}
