package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import in.co.indusnet.rekyc.model.TBLRequestlog;

@Transactional
public interface TblRequestLogRepository extends JpaRepository<TBLRequestlog, Long> {

	@Modifying(clearAutomatically = true)
	@Query(value = "update tbl_request_log SET is_address_modify = ?2, address=?1 where id=?3", nativeQuery = true)
	void newAddressAdded(String newAddress, boolean isAddressModify, long id);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update tbl_request_log SET status = ?2 where id=?1", nativeQuery = true)
	void statusUpdate(long id, int status);
	
	public TBLRequestlog findById(long id);

}
