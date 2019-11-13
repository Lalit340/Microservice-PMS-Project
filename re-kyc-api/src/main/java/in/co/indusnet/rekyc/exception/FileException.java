package in.co.indusnet.rekyc.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int errorCode;

	public FileException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
