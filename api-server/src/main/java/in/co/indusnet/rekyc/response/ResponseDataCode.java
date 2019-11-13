package in.co.indusnet.rekyc.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ResponseDataCode {

private String statusMessage;
private HttpStatus statusCode;

}
