package com.spring;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.spring.entity.AuditorAwareImpl;

/**
 * 
 * @author Zakir
 *
 */

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SpringBootRestApiRunner  {
	
	public static void main(String args[]){
		SpringApplication.run(SpringBootRestApiRunner.class, args);
	}
	
	@Bean
	AuditorAware<String>  auditorProvider() {
		return new AuditorAwareImpl();
	}
	
}
