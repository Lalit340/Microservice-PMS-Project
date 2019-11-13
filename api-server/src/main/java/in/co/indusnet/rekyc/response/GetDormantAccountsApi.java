package in.co.indusnet.rekyc.response;

import java.util.ArrayList;
import java.util.List;

import in.co.indusnet.rekyc.dto.CifList;
import in.co.indusnet.rekyc.dto.DormantAccountList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetDormantAccountsApi {

	private List<CifList> cifList = new ArrayList<>();

	private List<DormantAccountList> dormantAccountList = new ArrayList<>();

}
