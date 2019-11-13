package in.co.indusnet.soap.repository.fdcmi;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblErrorMessages;

public interface FdcmiTblErrorMessagesRepository extends JpaRepository<FdcmiTblErrorMessages , Long> {
	
	public FdcmiTblErrorMessages findByErrorCode(String errorCode);
	
}
