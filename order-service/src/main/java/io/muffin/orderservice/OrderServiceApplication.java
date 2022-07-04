package io.muffin.orderservice;

import brave.sampler.Sampler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.ecommercecommons.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "io.muffin.ecommercecommons.feign")
public class OrderServiceApplication {

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

	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
