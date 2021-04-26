package com.dazt.msscingredientsconversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsscIngredientsConversionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsscIngredientsConversionApplication.class, args);
	}

}
