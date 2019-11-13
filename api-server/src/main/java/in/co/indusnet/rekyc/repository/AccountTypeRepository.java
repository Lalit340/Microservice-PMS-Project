package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.rekyc.model.TblAccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<TblAccountType, Long>{
	
	public TblAccountType findByAccountCode(String accountCode);
}
