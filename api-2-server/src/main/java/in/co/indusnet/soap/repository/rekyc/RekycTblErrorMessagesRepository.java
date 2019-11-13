package in.co.indusnet.soap.repository.rekyc;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.indusnet.soap.model.rekyc.RekycTblErrorMessages;

public interface RekycTblErrorMessagesRepository extends JpaRepository<RekycTblErrorMessages , Long> {
	
	public RekycTblErrorMessages findByErrorCode(String errorCode);
	
}
