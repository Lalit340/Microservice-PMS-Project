package in.co.indusnet.rekyc.utility;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class Converter {

	@Autowired
	private ObjectMapper mapper;

	public static String xmlToJson(String xmlData) {
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode node = null;
		try {
			node = xmlMapper.readTree(xmlData.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper jsonMapper = new ObjectMapper();
		String json = null;
		try {
			json = jsonMapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public JsonNode getParamJsonValue(String jsonData, String data) {
		JsonNode node = null;
		JsonNode val = null;
		try {
			node = mapper.readTree(jsonData);
			val = node.findValue(data);
			return val;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}
	public boolean checkParamJsonValue(String jsonData, String data) {
		JsonNode node = null;
		boolean val = false;
		try {
			node = mapper.readTree(jsonData);
			val = node.has(data);
			return val;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}
}
