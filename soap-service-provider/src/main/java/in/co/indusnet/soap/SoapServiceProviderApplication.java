package in.co.indusnet.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//@EnableDiscoveryClient
@SpringBootApplication
public class SoapServiceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoapServiceProviderApplication.class, args);
	}

}