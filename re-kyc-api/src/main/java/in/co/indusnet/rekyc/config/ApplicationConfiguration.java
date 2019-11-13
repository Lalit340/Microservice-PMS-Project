package in.co.indusnet.rekyc.config;

import org.apache.commons.net.ftp.FTPClient;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class ApplicationConfiguration {
	
	
	/* 
	 * purpose to create a bean for mapping object values
	 * @return the ModelMapper() object .
	 * */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		
		RestTemplate restTemplate = new RestTemplate();
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(15000);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(15000);
	    return restTemplate;
	}
	
	@Bean
	public FTPClient fTPClient() {
		return new FTPClient();
	}
	
	@Bean
	public  ObjectMapper  objectMapper() {
		return new  ObjectMapper();
	}
		
	
	
	
}
