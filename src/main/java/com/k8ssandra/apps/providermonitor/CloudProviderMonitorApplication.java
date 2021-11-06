package com.k8ssandra.apps.providermonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CloudProviderMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudProviderMonitorApplication.class, args);
	}

}
