package in.co.indusnet.rekyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import in.co.indusnet.rekyc.model.TblAccountDetails;
import in.co.indusnet.rekyc.model.TblCifDetails;

@Transactional
public interface TblAccountDetailsRepository extends JpaRepository<TblAccountDetails, Long> {

	public List<TblAccountDetails> findByTblCifDetails(TblCifDetails tblCifDetails);
	
	@Query(value = "select * from tbl_account_details where is_display=?3 and status = ?1 and cif_id = ?2", nativeQuery = true)
    public List<TblAccountDetails> findAccountByCifwithStatus(String status, long cif, int display);
	
	@Query(value = "select * from tbl_account_details where is_display=1 and status = \"Account Is Active\" and cif_id = ?1 order by id asc limit 1", nativeQuery = true)
    public TblAccountDetails findFirstActiveAccountByCif(long cif);
	
	@Query(value = "select * from tbl_account_details where status = ?1 and cif_id = ?2 and request_dormant_active =\"1\"", nativeQuery = true)
    public List<TblAccountDetails> findDormantAccountWithActivateRequest(String status, long cif);
	
	@Query(value = "select count(id) from tbl_account_details where is_display=?3 and status = ?1 and cif_id = ?2", nativeQuery = true)
	long countByCifwithStatus(String status, long cif, int display);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update tbl_account_details SET request_dormant_active = CASE WHEN tbl_account_details.id IN ?1 THEN \"1\" ELSE \"0\" END "
			+ "WHERE tbl_account_details.cif_id=?2", nativeQuery = true)
	void markAccAsActiveRequest(List<Long> accountId, long cifId);
	
	
}
