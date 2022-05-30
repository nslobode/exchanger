package org.exchange.exchanger;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.exchange.exchanger.mappers.YamlConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExchangerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangerApplication.class, args);
	}

	@Bean
	public YamlConverter yamlHttpMessageConverter() {

		YAMLMapper mapper = new YAMLMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		return new YamlConverter(mapper);
	}

}
