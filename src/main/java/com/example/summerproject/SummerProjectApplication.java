package com.example.summerproject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Order Ayo rental API", version = "1.0", description = "Allows the Shop owner to keep track of the sales and Transactions associated with it.")
//        , servers = {@Server(url = "", description = "Deployed Server URL"),
//        @Server(url = "http://localhost:8080", description = "Local Server URL")
//}
)
@SecurityScheme(name = "SummerProject", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SummerProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SummerProjectApplication.class, args);
    }

}
