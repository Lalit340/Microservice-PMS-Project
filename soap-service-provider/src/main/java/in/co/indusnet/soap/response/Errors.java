package in.co.indusnet.soap.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "errors")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Errors  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Error error;
	
}
