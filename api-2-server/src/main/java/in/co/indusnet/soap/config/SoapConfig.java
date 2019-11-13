package in.co.indusnet.soap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import in.co.indusnet.soap.service.FinacleClient;
import in.co.indusnet.soap.service.IndusWebClient;
import in.co.indusnet.soap.service.NsdlClient;
import in.co.indusnet.soap.service.OtpClient;
import in.co.indusnet.soap.service.TalismaClient;


@Configuration
public class SoapConfig {
	
	@Bean(name = "indusweb")
	public Jaxb2Marshaller indusWebMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.indusweb.wsdl");
		return marshaller;
	}
	
	@Bean
	public IndusWebClient getIndusWebClient(Jaxb2Marshaller indusweb) {
		IndusWebClient indusWebClient = new IndusWebClient();
		indusWebClient.setMarshaller(indusweb);
		indusWebClient.setUnmarshaller(indusweb);
		return indusWebClient;
	}

	@Bean(name = "finacle")
	public Jaxb2Marshaller finacleMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.finacle.wsdl");
		return marshaller;
	}

	@Bean
	public FinacleClient getFinacleClient(Jaxb2Marshaller finacle) {
		FinacleClient finacleClient = new FinacleClient();
		finacleClient.setMarshaller(finacle);
		finacleClient.setUnmarshaller(finacle);
		return finacleClient;
	}
	
	@Bean(name = "nsdl")
	public Jaxb2Marshaller nsdlMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.nsdl.wsdl");
		return marshaller;
	}

	@Bean
	public NsdlClient nsdlClient(Jaxb2Marshaller nsdl) {
		NsdlClient nsdlClient = new NsdlClient();
		nsdlClient.setMarshaller(nsdl);
		nsdlClient.setUnmarshaller(nsdl);
		return nsdlClient;
	}
	
	@Bean(name = "otp")
	public Jaxb2Marshaller otpMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.otp.wsdl");
		return marshaller;
	}

	@Bean
	public OtpClient otpClient(Jaxb2Marshaller otp) {
		OtpClient otpClient = new OtpClient();
		otpClient.setMarshaller(otp);
		otpClient.setUnmarshaller(otp);
		return otpClient;
	}

	@Bean(name = "talisma")
	public Jaxb2Marshaller talismaMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("soap.talisma.wsdl");
		return marshaller;
	}
	
	@Bean
	public TalismaClient getTalismaClient(Jaxb2Marshaller talisma) {
		TalismaClient talismaClient = new TalismaClient();
		talismaClient.setMarshaller(talisma);
		talismaClient.setUnmarshaller(talisma);
		return talismaClient;
	}
}
