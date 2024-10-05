package com.gabriel.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GabrielDonationCampaignApplication {

	public static void main(String[] args) {
		SpringApplication.run(GabrielDonationCampaignApplication.class, args);
		System.out.println("http://localhost:8080");
	}

}
