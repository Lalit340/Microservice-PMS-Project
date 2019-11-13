package in.co.indusnet.rekyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import in.co.indusnet.rekyc.model.TblCifDetails;
import in.co.indusnet.rekyc.model.TblRequestOtp;

@Transactional
public interface TblRequestOtpRepository extends JpaRepository<TblRequestOtp, Long> {
	public TblRequestOtp findById(long id);
}
