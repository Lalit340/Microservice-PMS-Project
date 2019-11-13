package in.co.indusnet.soap.repository.fdcmi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblRequestToKey;

@Repository
public interface FdcmiTblRequestKeyRepository extends JpaRepository<FdcmiTblRequestToKey , Long> {
	
	
	public FdcmiTblRequestToKey findByToken(String token);
	
}
