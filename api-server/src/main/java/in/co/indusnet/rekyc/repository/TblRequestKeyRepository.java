package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import in.co.indusnet.rekyc.model.TblRequestToKey;
@Transactional
public interface TblRequestKeyRepository extends JpaRepository<TblRequestToKey , Long> {
	
	
	public TblRequestToKey findByToken(String token);
	
//	public TblRequestToKey getTblRequestToKeyByIdAndTBLRequestlog_ReqId(Long reqId);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update tbl_request_to_key SET token_status = ?2 where req_id=?1", nativeQuery = true)
	void statusUpdate(long req_id, int status);

}
