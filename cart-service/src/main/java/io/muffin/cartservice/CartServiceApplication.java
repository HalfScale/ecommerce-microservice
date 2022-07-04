package io.muffin.cartservice;

import brave.sampler.Sampler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients(basePackages = "io.muffin.ecommercecommons.feign")
@EnableEurekaClient
public class CartServiceApplication {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Sampler simpleSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}
