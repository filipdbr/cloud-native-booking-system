package reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Triggers Spring Boot's auto-configuration, component scanning, and embedded server setup
@SpringBootApplication
public class ReservationApplication {

    // Main entry point of the application, just like in C++ or Python
    public static void main(String[] args) {

        // Boots up the Spring framework and starts the embedded HTTP server (Tomcat on port 8080)
        SpringApplication.run(ReservationApplication.class, args);
    }
}