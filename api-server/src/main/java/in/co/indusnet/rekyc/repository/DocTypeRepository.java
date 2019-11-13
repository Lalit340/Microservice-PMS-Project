package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.rekyc.model.TblDocType;
@Repository
public interface DocTypeRepository extends JpaRepository<TblDocType, Long>{
	
}
