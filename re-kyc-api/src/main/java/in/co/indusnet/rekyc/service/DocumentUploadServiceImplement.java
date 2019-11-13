package in.co.indusnet.rekyc.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import in.co.indusnet.rekyc.dto.CifList;
import in.co.indusnet.rekyc.dto.TblDocDto;
import in.co.indusnet.rekyc.dto.TblDocTypeDto;
import in.co.indusnet.rekyc.dto.TblSettingsDto;
import in.co.indusnet.rekyc.exception.FileException;
import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblCifDetails;
import in.co.indusnet.rekyc.model.TblDoc;
import in.co.indusnet.rekyc.model.TblDocProofType;
import in.co.indusnet.rekyc.model.TblDocType;
import in.co.indusnet.rekyc.model.TblDocumentMapper;
import in.co.indusnet.rekyc.model.TblSettings;
import in.co.indusnet.rekyc.repository.DocTypeRepository;
import in.co.indusnet.rekyc.repository.DocumentMapperRepository;
import in.co.indusnet.rekyc.repository.ProofTypeRepository;
import in.co.indusnet.rekyc.repository.SettingsRepository;
import in.co.indusnet.rekyc.repository.TblCifDetailsRepository;
import in.co.indusnet.rekyc.repository.TblDocRepository;
import in.co.indusnet.rekyc.repository.TblRequestLogRepository;
import in.co.indusnet.rekyc.response.DocumentListing;
import in.co.indusnet.rekyc.response.DocumentUploadedApi;
import in.co.indusnet.rekyc.response.ResponseData;
import in.co.indusnet.rekyc.utility.AllTokenCreateAndValidate;
import in.co.indusnet.rekyc.utility.CommonFunctions;
import in.co.indusnet.rekyc.utility.GetErrorMessage;
import in.co.indusnet.rekyc.utility.ResponseHelper;

@Configuration
@PropertySource("classpath:constant/doctypeconstant.properties")
@PropertySource("classpath:constant/constantEnv.properties")
@PropertySource("classpath:constant/constant.properties")
@Service
public class DocumentUploadServiceImplement implements DocumentUploadService {

	@Autowired
	private AllTokenCreateAndValidate allTokenCreateAndValidate;

	@Autowired
	private TblDocRepository tblDocRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private DocTypeRepository docTypeRepository;

	@Autowired
	private GetErrorMessage getErrorMessage;

	@Autowired
	private TblCifDetailsRepository tblCifDetailsRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private DocumentMapperRepository documentMapperRepository;

	@Autowired
	private ProofTypeRepository proofTypeRepository;

	@Autowired
	private TblRequestLogRepository tblRequestLogRepository;

	private final Path fileLocation = Paths.get("Assets/Uploads");

	@Override
	public ResponseData uploadFiles(int idProofDocType, MultipartFile[] idProofFiles, int addressProofDocType,
			MultipartFile[] addressProofFiles, int nrProofDocType, MultipartFile[] nrProofFiles,
			int overseasProofDocType, MultipartFile[] overseasProofFiles, boolean isAddressProofSameAsIdProof,
			String authorization) {

		if (!Files.exists(fileLocation)) {
			try {
				Files.createDirectories(fileLocation);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}
		if (authorization.length() == 0 || authorization == null) {
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"),
					HttpStatus.UNAUTHORIZED.value());
		}

		long reqId = allTokenCreateAndValidate.validateToken(authorization);
		if (reqId == 0) {
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"),
					HttpStatus.UNAUTHORIZED.value());
		}
		List<TblSettings> settingList;
		settingList = settingsRepository.findAll();

		if (idProofFiles.length > settingList.get(0).getFileCountLimit()) {
			throw new FileException("File should be less than " + settingList.get(0).getFileCountLimit(), 401);
		}
		if (addressProofFiles.length > settingList.get(0).getFileCountLimit()) {
			throw new FileException("File should be less than " + settingList.get(0).getFileCountLimit(), 401);
		}
		if (nrProofFiles.length > settingList.get(0).getFileCountLimit()) {
			throw new FileException("File should be less than " + settingList.get(0).getFileCountLimit(), 401);
		}
		if (overseasProofFiles.length > settingList.get(0).getFileCountLimit()) {
			throw new FileException("File should be less than " + settingList.get(0).getFileCountLimit(), 401);
		}

		List<TblDocProofType> docProofList;
		docProofList = proofTypeRepository.findAll();
		int available = Integer.parseInt(environment.getProperty("Available"));

		if (idProofDocType != 0 && idProofFiles.length == 0) {
			long uploadedFileCount = tblDocRepository.countByReqIdAndDocProofTypeAndStatus(reqId,
					docProofList.get(0).getId(), available);
			System.out.println("uploadedFileCount id proof::" + uploadedFileCount);
			if (uploadedFileCount == 0) {
				throw new FileException("Atleast one ID proof is required", 401);
			}
		} else {
			for (MultipartFile file : idProofFiles) {

				singleFileUpload(file, docProofList.get(0).getId(), reqId, idProofDocType, isAddressProofSameAsIdProof);
			}
		}

		if (addressProofDocType != 0 && addressProofFiles.length == 0 && isAddressProofSameAsIdProof == false) {
			long uploadedFileCount = tblDocRepository.countByReqIdAndDocProofTypeAndStatus(reqId,
					docProofList.get(1).getId(), available);
			System.out.println("uploadedFileCount Address proof::" + uploadedFileCount);
			if (uploadedFileCount == 0) {
				throw new FileException("Atleast one Address proof is required", 401);
			}
		} else {
			for (MultipartFile file : addressProofFiles) {

				singleFileUpload(file, docProofList.get(1).getId(), reqId, addressProofDocType,
						isAddressProofSameAsIdProof);
			}
		}

		if (nrProofDocType != 0 && nrProofFiles.length == 0) {
			long uploadedFileCount = tblDocRepository.countByReqIdAndDocProofTypeAndStatus(reqId,
					docProofList.get(2).getId(), available);
			System.out.println("uploadedFileCount NR proof::" + uploadedFileCount);
			if (uploadedFileCount == 0) {
				throw new FileException("Atleast one NR proof is required", 401);
			}
		} else {
			for (MultipartFile file : nrProofFiles) {

				singleFileUpload(file, docProofList.get(2).getId(), reqId, nrProofDocType, isAddressProofSameAsIdProof);
			}
		}

		if (overseasProofDocType != 0 && overseasProofFiles.length == 0) {
			long uploadedFileCount = tblDocRepository.countByReqIdAndDocProofTypeAndStatus(reqId,
					docProofList.get(3).getId(), available);
			System.out.println("uploadedFileCount overseas proof::" + uploadedFileCount);
			if (uploadedFileCount == 0) {
				throw new FileException("Atleast one Overseas proof is required", 401);
			}
		} else {
			for (MultipartFile file : overseasProofFiles) {

				singleFileUpload(file, docProofList.get(3).getId(), reqId, overseasProofDocType,
						isAddressProofSameAsIdProof);
			}
		}

		if (idProofFiles.length == 0 && addressProofFiles.length == 0 && nrProofFiles.length == 0
				&& overseasProofFiles.length == 0) {
			long uploadedFileCount = tblDocRepository.countByReqIdAndStatus(reqId, available);
			if (uploadedFileCount == 0) {
				throw new FileException("No files found", 401);
			}
		}

		ResponseData response = ResponseHelper.responseSender("File uploaded successfully", 200);
		return response;
	}

	public void singleFileUpload(MultipartFile file, int docProofType, long reqId, int verificationDocType,
			boolean isAddressProofSameAsIdProof) {

		TBLRequestlog tblRequestLog = tblRequestLogRepository.findById(reqId);
		List<TblSettings> settingList;
		settingList = settingsRepository.findAll();
		TblDoc uploadFile = new TblDoc();
		float fileSize = file.getSize() / (1024 * 1024);
		if (fileSize > (float) settingList.get(0).getSingleFileUploadMaxSize()) {
			throw new FileException(
					"File Size Should be less than " + settingList.get(0).getSingleFileUploadMaxSize() + "MB", 401);
		}
		UUID uuid = UUID.randomUUID();
		String fileType = file.getContentType();
		String fileExtention = (fileType.substring(fileType.indexOf("/"))).replace('/', '.');
		String uniqueId = uuid.toString();
		String fileNameWithExtention = uniqueId + fileExtention;
		int available = Integer.parseInt(environment.getProperty("Available"));
		System.out.println("Name WE :: " + fileNameWithExtention);
		if (file.getContentType().equals("application/pdf") || file.getContentType().equals("image/jpeg")
				|| file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")) {
			try {
				Files.copy(file.getInputStream(), fileLocation.resolve(fileNameWithExtention),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new FileException("Invalid File Path", 401);
			}
			uploadFile.setUploadedFileName(fileNameWithExtention);
			uploadFile.setOriginalFileName(file.getOriginalFilename());
			long size = file.getSize();
			uploadFile.setFileSize(size);
			uploadFile.setDocTypeId(verificationDocType);
			uploadFile.setDocProofType(docProofType);
			uploadFile.setCreatedAt(LocalDateTime.now());
			uploadFile.setReqId(reqId);
			uploadFile.setStatus(available);
			uploadFile = tblDocRepository.save(uploadFile);
			tblRequestLog.setAddressProofSameAsIdProof(isAddressProofSameAsIdProof);
			tblRequestLog.setStatus(Integer.parseInt(environment.getProperty("statusUploadDocument")));
			tblRequestLogRepository.save(tblRequestLog);
		} else {
			throw new FileException("File Should be in pdf,jpeg,jpg,png", 401);
		}
	}

	@Override
	public ResponseData deleteFile(String token, long fileId) {
		Optional<TblDoc> file = tblDocRepository.findById(fileId);
		file.orElseThrow(() -> new FileException("File does not exist", 401));
		file.get().setStatus(Integer.parseInt(environment.getProperty("imageDeleteStatus")));
		tblDocRepository.save(file.get());
		ResponseData response = ResponseHelper.responseSender("File Deleted Successfully", 200);
		return response;
	}

	@Override
	public DocumentUploadedApi getUploadedDocumentList(String token) {

		if (token.length() == 0 || token == null)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"),
					HttpStatus.UNAUTHORIZED.value());

		long reqId = allTokenCreateAndValidate.validateToken(token);
		// if condition to validate token return data
		if (reqId == 0)
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"),
					HttpStatus.UNAUTHORIZED.value());

		long cifId = tblCifDetailsRepository.selectedCif(reqId);
		String checkIsNre = this.checkIsNre(token, cifId, environment.getProperty("myServiceType"));
		if (checkIsNre.equals("error"))
			throw new ServiceException(getErrorMessage.getErrorMessages("UNABLE_TO_PROCESS_ERR"),
					HttpStatus.UNAUTHORIZED.value());

		List<TblDocumentMapper> docMapList;
		List<TblDoc> documentList;
		List<TblSettings> settingList;
		Optional<TblDocType> docTypeList;
		List<TblDocProofType> docProofList;

		String idProof = "idProof";
		String addressProof = "addressProof";
		String nrProof = "nrProof";
		String overseasProof = "overseasProof";

		int available = Integer.parseInt(environment.getProperty("Available"));
		DocumentUploadedApi docUploaded = new DocumentUploadedApi();

		docProofList = proofTypeRepository.findAll();
		TBLRequestlog tblRequestLog = tblRequestLogRepository.findById(reqId);
		docUploaded.setIsAddressProofSameAsIdProof(tblRequestLog.isAddressProofSameAsIdProof());
//		Map<String, String> getDocTypes = new HashMap<String, String>();
//		
//		try {
//			URL propFileUrl = Resources.getResource("constant/doctypeconstant.properties");
//			String docPath = Paths.get(propFileUrl.toURI()).toString();
//			getDocTypes = CommonFunctions.readPropertiesFileAsMap(docPath, "=");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		

		List<TblCifDetails> tblCifDetails = tblCifDetailsRepository.findById(cifId);
		for (TblCifDetails obj : tblCifDetails) {
			// Object creation
			CifList cifList = new CifList();
			cifList.setId(obj.getId());
			cifList.setCifCode("Customer ID: CIF " + CommonFunctions.maskCardNumber(obj.getCifCode(), "xxxx####"));
			cifList.setSelected(obj.isSelected());
			cifList.setRiskProfile(obj.getRiskProfile());
			docUploaded.getCifList().add(cifList);
		}

		for (TblDocProofType entry : docProofList) {
			DocumentListing docListing = new DocumentListing();
			Integer verificationDocType = entry.getId();
			docMapList = documentMapperRepository.findByDocProofTypeId(verificationDocType);

			for (TblDocumentMapper mapDetails : docMapList) {
				docTypeList = docTypeRepository.findById((long) mapDetails.getDocTypeId());

				TblDocTypeDto docTypes = new TblDocTypeDto();
				docTypes.setId(docTypeList.get().getId());
				docTypes.setCode(docTypeList.get().getCode());
				docTypes.setTitle(docTypeList.get().getTitle());
				docListing.getDocTypeList().add(docTypes);
			}

			settingList = settingsRepository.findAll();
			for (TblSettings obj : settingList) {
				TblSettingsDto settings = new TblSettingsDto();

				String[] supportedFileFormat = obj.getSupportedFileFormats().split(",");
				settings.setFileCountLimit(obj.getFileCountLimit());
				settings.setSingleFileUploadMaxSize((obj.getSingleFileUploadMaxSize()));
				settings.setSupportedFileFormats(supportedFileFormat); // it should be array
				settings.setOverseasShow(obj.isOverseasShow());
				docListing.setAppSettings(settings);
			}

			documentList = tblDocRepository.findByReqIdAndDocProofTypeAndStatus(reqId, verificationDocType, available);
			for (TblDoc document : documentList) {
				TblDocDto docs = new TblDocDto();

				docs.setId(document.getId());
				docs.setFileSize(document.getFileSize());
				docs.setOriginalFileName(document.getOriginalFileName());
				// docs.setStatus(document.getStatus());
				docs.setUploadedFileName(document.getUploadedFileName());
				docs.setDocTypeId(document.getDocTypeId());
				docListing.getUploadedDocs().add(docs);
			}

			if (entry.getDocType().equals(idProof)) {
				docUploaded.setIdProof(docListing);
			} else if (entry.getDocType().equals(addressProof)) {
				docUploaded.setAddressProof(docListing);
			} else if (entry.getDocType().equals(nrProof)) {
				docUploaded.setNrProof(docListing);
			} else if (entry.getDocType().equals(overseasProof)) {
				docUploaded.setOverseasProof(docListing);
			}
			String custType = "";
			if (checkIsNre.equals("Y"))
				custType = "NR";
			docUploaded.setCustomerType(custType);

		}
		return docUploaded;

	}

	public String checkIsNre(String token, long cifId, String serviceType) {

		List<TblCifDetails> selectedCif = tblCifDetailsRepository.findById(cifId);
		boolean isNreChecked = false;
		boolean isNrFromDb = false;
		String isNreFromDb = "N";
		for (TblCifDetails obj : selectedCif) {
			isNreChecked = obj.isNrChecked();
			isNrFromDb = obj.isNr();
		}

		if (isNreChecked) {
			if (isNrFromDb)
				isNreFromDb = "Y";
			return isNreFromDb;
		} else {
			String cif = tblCifDetailsRepository.getCifCode(cifId);
			String uri = environment.getProperty("soapServiceUrl") + "finacle/isnre";
			// create a list the headers
			HttpHeaders headers = new HttpHeaders();
			headers.set("authToken", token);
			headers.set("serviceType", serviceType);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("cifId", cif);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

			// initialize RestTemplate
			RestTemplate restTemplate = new RestTemplate();
			// post the request. The response should be JSON string
			ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
			System.out.println("isnre response==============================> " + response);
			int responseCode = response.getStatusCodeValue();
			if (responseCode == 200) {
				String isNRE = response.getBody();
				/* save is nre flag of selected cif */
				Boolean isNr = false;
				if (isNRE.equals("Y"))
					isNr = true;

				List<TblCifDetails> cifDetails = tblCifDetailsRepository.findById(cifId);
				for (TblCifDetails obj : cifDetails) {
					obj.setNr(isNr);
					obj.setNrChecked(true);
					tblCifDetailsRepository.save(obj);
				}
				return isNRE;
			} else {
				return "error";
			}
		}
	}

}
