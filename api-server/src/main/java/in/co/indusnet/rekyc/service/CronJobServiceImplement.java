package in.co.indusnet.rekyc.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import in.co.indusnet.rekyc.exception.ServiceException;
import in.co.indusnet.rekyc.model.TBLRequestlog;
import in.co.indusnet.rekyc.model.TblAccountDetails;
import in.co.indusnet.rekyc.model.TblCifDetails;
import in.co.indusnet.rekyc.model.TblDoc;
import in.co.indusnet.rekyc.model.TblRequestToKey;
import in.co.indusnet.rekyc.repository.AccountTypeRepository;
import in.co.indusnet.rekyc.repository.TblAccountDetailsRepository;
import in.co.indusnet.rekyc.repository.TblCifDetailsRepository;
import in.co.indusnet.rekyc.repository.TblDocRepository;
import in.co.indusnet.rekyc.repository.TblRequestKeyRepository;
import in.co.indusnet.rekyc.repository.TblRequestLogRepository;

@Service("cronservice")
public class CronJobServiceImplement implements CronJobService {

	@Autowired
	private TblRequestKeyRepository tblKeyRepository;

	@Autowired
	private FTPClient ftpClient;

	@Autowired
	private TblDocRepository tblDocRepository;

	@Autowired
	private TblRequestLogRepository tblRequestLogRepository;

	@Autowired
	private TblCifDetailsRepository tblCifDetailsRepository;

	private final Path fileLocation = Paths.get("Assets/Uploads");

	/*
	 * 
	 * this method for scheduling time wise and updating token status
	 */
	@Scheduled(cron = "0 * * * * ?")
	@Override
	public void schedulerStatusUpdate() {
		// getting all data to the tbl_Request_To_Key table
		List<TblRequestToKey> tblRequestToKey = tblKeyRepository.findAll();

		if (tblRequestToKey != null) {
			// Lambda to get each object to the List
			tblRequestToKey.stream().forEach(requestKeyObject -> {

				if (requestKeyObject.isTokenStatus()) {
					// database create time
					LocalDateTime createTime = requestKeyObject.getCreatedAt();
					System.out.println("database time ::" + createTime);
					// present time
					LocalDateTime resentTime = LocalDateTime.now();
					System.out.println("resent time ::" + resentTime);

					// Duration Object to compare between to time
					Duration duration = Duration.between(createTime, resentTime);
					// duration in minutes
					long minutes = duration.toMinutes();
					System.out.println("Duration minute ::" + minutes);

					if (minutes >= 10) {
						// Updating token status
						System.out.println("yes condition is true");
						requestKeyObject.setTokenStatus(false);
						tblKeyRepository.save(requestKeyObject);
					} else
						System.out.println("yes condition is false");

				}
			});
		} else {
			throw new ServiceException("Tbl Request To Key table data not found ", HttpStatus.UNAUTHORIZED.value());
		}
	}

	@Override
	public void setup(Resource file) {

		try {
			System.out.println("connection start");

			ftpClient.connect("10.0.4.81", 21);
			System.out.println("connected");
			ftpClient.login("test_user", "12345678");
			System.out.println("connected user");
			ftpClient.enterLocalPassiveMode();
			System.out.println("Working directory ::" + ftpClient.printWorkingDirectory());
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			ftpClient.changeWorkingDirectory("Test");

			// APPROACH #1: uploads first file using an InputStream
			String fileName = LocalTime.now() + "_" + file.getFile().getName();
			boolean result = ftpClient.storeFile(fileName, file.getInputStream());

			if (result)
				System.out.println("The file is uploaded successfully.");
			else
				System.out.println("file not stored");

			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void getResourceData() {
		List<TblDoc> list = tblDocRepository.findByStatus(1);
		for (TblDoc tbl : list) {
			if (tbl.getStatus() == 1) {
				TBLRequestlog tblReq = tblRequestLogRepository.findById(tbl.getReqId());
				if (tblReq.getStatus() == 8) {
					List<TblCifDetails> cifList = tblCifDetailsRepository.findByTblRequestLog(tblReq);
					for (TblCifDetails cif : cifList) {
						if (cif.isSelected()) {
							System.out.println(tbl.getUploadedFileName());
							Path imagePath = fileLocation.resolve(tbl.getUploadedFileName());
							try {
								Resource resource = new UrlResource(imagePath.toUri());

								if (resource.exists() || resource.isReadable()) {
									setup(resource);

								}
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}

						}
					}

				}
			}
		}

	}

	@Override
	public TblRequestToKey getValue() {
//		TBLRequestlog tblLogDetails = tblRequestLogRepository.findById(1);
//		TblRequestToKey tblKeyDetails = tblKeyRepository.getTblRequestToKeyByIdAndTBLRequestlog_ReqId(1l);
//		System.out.println(" hi data ::" + tblKeyDetails);
		return null;
	}

}
