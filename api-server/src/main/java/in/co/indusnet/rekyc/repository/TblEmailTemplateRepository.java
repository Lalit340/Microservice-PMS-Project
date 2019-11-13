package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.indusnet.rekyc.model.TblEmailTemplate;

public interface TblEmailTemplateRepository extends JpaRepository<TblEmailTemplate , Long> {
		
	public TblEmailTemplate findByEmailType(String errorCode);
	
}
