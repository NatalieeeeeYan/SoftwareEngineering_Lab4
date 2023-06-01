package com.team.ShopSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@MapperScan("com.team.ShopSystem.*.mapper")
@ComponentScan(basePackages = {"com.team.ShopSystem.*"})
@EnableScheduling
public class ShopSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopSystemApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}