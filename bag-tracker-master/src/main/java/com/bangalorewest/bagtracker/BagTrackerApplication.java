package com.bangalorewest.bagtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bangalorewest.bagtracker.util.ConfigReader;

/**
 * @author sudhanshu.singh
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(ConfigReader.class)
public class BagTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BagTrackerApplication.class, args);
	}
	
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
