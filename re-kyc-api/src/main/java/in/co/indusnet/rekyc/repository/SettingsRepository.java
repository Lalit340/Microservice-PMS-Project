package in.co.indusnet.rekyc.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import in.co.indusnet.rekyc.model.TblSettings;

public interface SettingsRepository extends JpaRepository<TblSettings, Long>{
	
}
