package estoque.cerveja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@ComponentScan(basePackages = {"estoque.cerveja.controller", "estoque.cerveja.mapper",
		"estoque.cerveja.service", "estoque.cerveja.dto",
		"estoque.cerveja.enums", "estoque.cerveja.exception"})
@EntityScan(basePackages = { "estoque.cerveja.entity" })
@EnableJpaRepositories(basePackages = { "estoque.cerveja.repository" })

public class CervejaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CervejaApplication.class, args);
	}

}
