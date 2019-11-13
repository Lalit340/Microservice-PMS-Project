package in.co.indusnet.soap.repository.fdcmi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.co.indusnet.soap.model.fdcmi.FdcmiTblRequestResponseLog;

@Repository
public interface FdcmiLogRepository extends JpaRepository<FdcmiTblRequestResponseLog, Long>{

}