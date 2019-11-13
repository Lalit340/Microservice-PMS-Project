package in.co.indusnet.soap.utility;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;
import org.json.XML;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class Converter {

	@Autowired
	private ObjectMapper mapper;

	public static String xmlToJson(String xmlData) {
		System.out.println("xmlData ================> " + xmlData);
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode node = null;
		try {
			node = xmlMapper.readTree(xmlData.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("NODE ================> " + node);
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

	public static String xmlCollectionToJson(String xmlData) {
		JSONObject jObject = XML.toJSONObject(xmlData);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		Object json = null;
		try {
			json = mapper.readValue(jObject.toString(), Object.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String output = null;
		try {
			output = mapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    System.out.println(output);
		return output;

	}

	public JsonNode getParamJsonValue(String jsonData, String data) {
		JsonNode node = null;
		JsonNode riskProfileValues = null;
		try {
			node = mapper.readTree(jsonData);
			riskProfileValues = node.findValue(data);
			System.out.println("object Data ::" + node.findValue(data) + " value ............" + riskProfileValues);
			return riskProfileValues;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return riskProfileValues;
	}

	public static String jsonToXml(Object jsonData) {
		// converting to json object
		JSONObject obj = new JSONObject(jsonData);

		//converting json to xml
		String xmlData = XML.toString(obj);
//		System.out.println(obj+":: json data to xml ::"+xmlData);
		return xmlData;
	}
}
