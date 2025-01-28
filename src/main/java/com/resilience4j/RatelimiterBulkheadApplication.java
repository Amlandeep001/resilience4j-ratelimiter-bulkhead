package com.resilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RatelimiterBulkheadApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(RatelimiterBulkheadApplication.class, args);
	}
}
