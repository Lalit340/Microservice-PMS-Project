package in.co.indusnet.rekyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.rekyc.model.TblDoc;

@Repository
public interface TblDocRepository extends JpaRepository<TblDoc, Long>{
		
	public List<TblDoc> findByReqIdAndDocProofTypeAndStatus(Long reqId, Integer verificationDocType, Integer status); 
	
	public List<TblDoc> findByStatus(int status);
	
	long countByReqIdAndDocProofTypeAndStatus(long rId,Integer verificationDocType, Integer status);
	
	long countByReqIdAndStatus(long rId, Integer status);
}
