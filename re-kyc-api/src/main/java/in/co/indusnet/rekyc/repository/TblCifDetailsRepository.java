package in.co.indusnet.rekyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblCifDetails;

@Transactional
public interface TblCifDetailsRepository extends JpaRepository<TblCifDetails, Long> {

	public List<TblCifDetails> findByTblRequestLog(TBLRequestlog tblRequestLog);
	
	public List<TblCifDetails> findBytblRequestLog_rId(long rId);
	
	public List<TblCifDetails> findBytblRequestLog_rIdOrderByRiskProfileDesc(long rId);
	public List<TblCifDetails> findBytblRequestLog_rIdOrderByRiskProfileAsc(long rId);
	
	public List<TblCifDetails> findById(long id);
	
	long countBytblRequestLog_rId(long rId);
	
	public TblCifDetails findBytblRequestLog_rIdAndMasterCif(long rId, boolean isMaster);
		
	@Query(value = "select * from tbl_cif_details where is_selected = \"1\" and req_id = ?1", nativeQuery = true)
	long selectedCif(long reqid);
	
	
	@Query(value = "select * from tbl_cif_details where id = ?1 and req_id = ?2", nativeQuery = true)
    public List<TblCifDetails> findByidreqid(long id, long reqid);
	
	@Query(value = "select count(tcd.id) from tbl_cif_details tcd where tcd.id = ?1 and tcd.req_id = ?2", nativeQuery = true)
	long countByidreqid(long id, long reqid);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "update tbl_cif_details SET is_selected= CASE WHEN tbl_cif_details.id= :cifId THEN \"1\" ELSE \"0\" END", nativeQuery = true)
	void markEntryAsSelected(@Param("cifId") Long cifId);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_cif_details SET is_selected = \"1\" WHERE req_id = ?1 order by risk_profile DESC, id ASC limit 1", nativeQuery = true)
	void markEntryAsSelectedFirstTime(long reqid);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE tbl_cif_details SET is_pan_validated = ?2 WHERE req_id = ?1 and master_cif=0", nativeQuery = true)
	void updatePanValidation(long reqid, int isPan);
	
	@Query(value = "select cif_code from tbl_cif_details where id = ?1", nativeQuery = true)
    String getCifCode(long id);
	
	@Query(value = "select customer_name from tbl_cif_details where id = ?1", nativeQuery = true)
    String getCifName(long id);
	
	@Query(value = "select customer_name from tbl_cif_details where is_selected = \"1\" and req_id = ?1", nativeQuery = true)
    String getSelectedCifName(long reqid);
	
	@Query(value = "select count(tcd.id) from tbl_cif_details tcd where tcd.master_cif = \"1\" and tcd.req_id = ?1", nativeQuery = true)
	int isMasterPresent(long reqid);
	
}
