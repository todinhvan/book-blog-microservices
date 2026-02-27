package vn.van.identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IdentityServiceApplication {
    // spotless:off
	public static void main(String[] args) { SpringApplication.run(IdentityServiceApplication.class, args); }
    // spotless:on
}
