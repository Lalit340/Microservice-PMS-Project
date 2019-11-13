package in.co.indusnet.soap.repository.rekyc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.soap.model.rekyc.RekycTblRequestResponseLog;

@Repository
public interface RekycLogRepository extends JpaRepository<RekycTblRequestResponseLog, Long>{

}