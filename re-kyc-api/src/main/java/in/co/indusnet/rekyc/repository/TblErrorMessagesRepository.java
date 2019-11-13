package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.indusnet.rekyc.model.TblErrorMessages;


public interface TblErrorMessagesRepository extends JpaRepository<TblErrorMessages , Long> {
	
	
	public TblErrorMessages findByErrorCode(String errorCode);
	
}
