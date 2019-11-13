package in.co.indusnet.rekyc.response;

import java.util.HashMap;
import java.util.Map;


import org.springframework.stereotype.Component;


import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ResponseData {

private String statusMessage;
private int statusCode;
public Map<String, String> responseData = new HashMap<>();
}
