package in.co.indusnet.rekyc.response;

import java.util.ArrayList;
import java.util.List;

import in.co.indusnet.rekyc.dto.TblDocDto;
import in.co.indusnet.rekyc.dto.TblDocTypeDto;
import in.co.indusnet.rekyc.dto.TblSettingsDto;
//import in.co.indusnet.rekyc.model.TblDoc;
//import in.co.indusnet.rekyc.model.TblDocType;
//import in.co.indusnet.rekyc.model.TblSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DocumentListing {
	
	private List<TblDocDto> uploadedDocs = new ArrayList<>();
	
	private List<TblDocTypeDto> docTypeList = new ArrayList<>();
	
	private TblSettingsDto appSettings = new TblSettingsDto();

}
