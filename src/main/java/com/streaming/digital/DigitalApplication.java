package com.streaming.digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalApplication {

	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
		System.setProperty("https.cipherSuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
		SpringApplication.run(DigitalApplication.class, args);
	}

}
