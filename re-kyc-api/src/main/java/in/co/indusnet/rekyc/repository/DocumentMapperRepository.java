package in.co.indusnet.rekyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.indusnet.rekyc.model.TblDocumentMapper;

@Repository
public interface DocumentMapperRepository extends JpaRepository<TblDocumentMapper, Long>{

	//@Query("select doc_type_id from tbl_document_mapper where doc_proof_type_id=?1 order by doc_proof_type_id ASC")
	//public List<TblDocumentMapper> getDocTypeListByProofType(long proofId);
	
	public List<TblDocumentMapper> findByDocProofTypeId(int proofId);
}
