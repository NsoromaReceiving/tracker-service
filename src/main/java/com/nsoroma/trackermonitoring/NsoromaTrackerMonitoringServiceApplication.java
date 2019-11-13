package com.nsoroma.trackermonitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class NsoromaTrackerMonitoringServiceApplication implements ApplicationRunner {
	@Value("${spring.application.name}")
	private String name;
	
	public static void main(String[] args) {
		SpringApplication.run(NsoromaTrackerMonitoringServiceApplication.class, args);
	}
	@Override
	public void run(ApplicationArguments agr0) throws Exception {
		System.out.println("Starting Application ...");
		System.out.println("Application Name : " + name);
	}

}
