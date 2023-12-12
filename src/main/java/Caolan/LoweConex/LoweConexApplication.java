package Caolan.LoweConex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableCaching
public class LoweConexApplication{
	public static void main(String[] args) {
		SpringApplication.run(LoweConexApplication.class, args);
	}

}



