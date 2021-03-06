package br.com.hbparking;

import br.com.hbparking.file.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties({FileProperties.class})
public class HbparkingApplication {
    public static void main(String[] args) {
        SpringApplication.run(HbparkingApplication.class, args);
    }
}
