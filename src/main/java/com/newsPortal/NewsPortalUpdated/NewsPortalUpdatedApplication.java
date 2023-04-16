package com.newsPortal.NewsPortalUpdated;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NewsPortalUpdatedApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsPortalUpdatedApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
