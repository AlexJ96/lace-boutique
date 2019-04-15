package api.endpoint.endpoints;


import com.google.gson.Gson;

import api.endpoint.EndPoint;
import api.services.MiscService;
import api.utils.Utils;
import spark.Service;

public class MiscEndpoint implements EndPoint {
	
	private MiscService miscService = new MiscService();
	private Gson gson = new Gson();
	
	public MiscEndpoint() {
	}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/misc", () -> {
			
			spark.post("/get-page-content", (request, response) -> {
				return Utils.getJsonBuilder().toJson(miscService.getMiscPageForUrl(request.body()));
			});
			
		});
	}

}
