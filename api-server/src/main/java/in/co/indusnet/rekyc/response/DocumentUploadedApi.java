package in.co.indusnet.rekyc.response;

import java.util.ArrayList;

import java.util.List;

import in.co.indusnet.rekyc.dto.CifList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class DocumentUploadedApi {
	private List<CifList> cifList = new ArrayList<>();
	private DocumentListing idProof = new DocumentListing();
	private DocumentListing addressProof = new DocumentListing();
	private DocumentListing nrProof = new DocumentListing();
	private DocumentListing overseasProof = new DocumentListing();
	private String customerType;
	private Boolean isAddressProofSameAsIdProof;
}
