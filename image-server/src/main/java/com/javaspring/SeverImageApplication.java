package com.javaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.javaspring.properties.FileStorageProperties;


@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class SeverImageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeverImageApplication.class, args);
	}

}
