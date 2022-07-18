package io.muffin.cartservice;

import brave.sampler.Sampler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"io.muffin.cartservice", "io.muffin.ecommercecommons"})
@EnableFeignClients(basePackages = "io.muffin.ecommercecommons.feign")
@EnableEurekaClient
public class CartServiceApplication {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().findAndRegisterModules();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Sampler simpleSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}
