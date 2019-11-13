package in.co.indusnet.rekyc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.co.indusnet.rekyc.response.DocumentUploadedApi;
import in.co.indusnet.rekyc.response.ResponseData;

@Service
public interface DocumentUploadService {
	public DocumentUploadedApi getUploadedDocumentList(String token);

	public ResponseData uploadFiles(int idProofDocType, MultipartFile[] idProofFiles, int addressProofDocType,
			MultipartFile[] addressProofFiles, int nrProofDocType, MultipartFile[] nrProofFiles,
			int overseasProofDocType, MultipartFile[] overseasProofFiles, boolean isAddressProofSameAsIdProof,
			String authorization);

	public ResponseData deleteFile(String token, long fileId);
		
}
