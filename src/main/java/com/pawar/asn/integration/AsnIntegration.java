package com.pawar.asn.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AsnIntegration {

	public static void main(String[] args) {
		SpringApplication.run(AsnIntegration.class, args);
	}

}
