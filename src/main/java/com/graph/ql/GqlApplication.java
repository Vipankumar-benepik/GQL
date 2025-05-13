package com.graph.ql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GqlApplication.class, args);
	}

}
