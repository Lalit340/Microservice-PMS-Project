package in.co.indusnet.rekyc.service;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import in.co.indusnet.rekyc.response.GetCifDetailsApi;
import in.co.indusnet.rekyc.response.GetCifListApi;
import in.co.indusnet.rekyc.response.GetDormantAccountsApi;
import in.co.indusnet.rekyc.response.ResponseData;

@Service
public interface TblCifService {
	
	public GetCifListApi getCifList(String token);
	
	public GetCifDetailsApi getCifDetails(String token , long cifId);

	public ResponseData saveSelectedCif(String token, long cifId, String newAddress, boolean isAddressModify);
	
	public GetDormantAccountsApi getDormantList(String token);
	
	public ResponseData saveDormantAccounts(String token, Long cif, List<Long> accountId);
		
	//public Boolean saveLinkedCifToDb(String token, String serviceType); 
	
	//public Boolean saveCifToDb(String token, long reqId, String cif, String serviceType, String cifType, String masterCif);
	
	public ResponseData sendOtp(String token, int otpAttempts);
}
