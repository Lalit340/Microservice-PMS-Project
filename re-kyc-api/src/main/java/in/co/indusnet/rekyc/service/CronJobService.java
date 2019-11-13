package in.co.indusnet.rekyc.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.indusnet.rekyc.model.TblAccountDetails;
import in.co.indusnet.rekyc.model.TblRequestToKey;

@Service
public interface CronJobService {

	// this method for schedule time wise and updating token status
	public void schedulerStatusUpdate();

	//public void setup( MultipartFile file); 
	
	public void getResourceData();

	void setup(Resource file);
	
	public TblRequestToKey getValue();
		
}
