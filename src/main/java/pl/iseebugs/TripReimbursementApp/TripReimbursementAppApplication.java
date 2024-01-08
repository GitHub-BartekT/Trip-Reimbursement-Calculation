package pl.iseebugs.TripReimbursementApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
		scanBasePackages = {"pl.iseebugs.TripReimbursementApp"}
)
@EnableJpaRepositories(
		basePackages = {"pl.iseebugs.TripReimbursementApp.adapter"}
)
@EntityScan(
		basePackages = {"pl.iseebugs.TripReimbursementApp.model"}
)
public class TripReimbursementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripReimbursementAppApplication.class, args);
	}

}
