package io.github.alexwu727.authsystemeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AuthSystemEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthSystemEurekaServerApplication.class, args);
	}

}
