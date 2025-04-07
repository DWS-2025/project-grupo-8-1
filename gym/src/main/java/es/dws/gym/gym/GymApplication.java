package es.dws.gym.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

// Main class for starting the Gym application using Spring Boot.
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class GymApplication {

	// The entry point of the Gym application.
	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}
}

