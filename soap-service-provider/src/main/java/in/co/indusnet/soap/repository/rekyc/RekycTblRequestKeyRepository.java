package in.co.indusnet.soap.repository.rekyc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.soap.model.rekyc.RekycTblRequestToKey;

@Repository
public interface RekycTblRequestKeyRepository extends JpaRepository<RekycTblRequestToKey , Long> {

	public RekycTblRequestToKey findByToken(String token);
	
}
