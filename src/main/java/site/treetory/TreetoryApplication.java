package site.treetory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TreetoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreetoryApplication.class, args);
	}

}
