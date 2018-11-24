package api.endpoint.endpoints;

import api.endpoint.EndPoint;
import api.utils.Utils;
import spark.Service;

public class TestEndPoint implements EndPoint {
	
	public TestEndPoint() {
		
	}

	@Override
	public void configure(Service spark, String basePath) {		
		spark.post(basePath + "/test", (request, response) -> {
			return Utils.getJsonBuilder().toJson("Test Response Body");
		});
	}

}
