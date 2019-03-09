package api.endpoint.endpoints;

import org.bson.types.ObjectId;

import api.endpoint.EndPoint;
import api.services.TokenService;
import api.utils.Utils;
import spark.Service;

public class AuthToken implements EndPoint {
	
	public AuthToken() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/token", () -> {
			spark.get("/request-token", (request, response) -> {
				return Utils.getJsonBuilder().toJson(TokenService.generateToken(new ObjectId().toString(), 1L, null));
			});
			
			spark.post("/refresh-token", (request, response) -> {
				return Utils.getJsonBuilder().toJson(TokenService.refreshToken(request.headers("LBT").substring(14)));
			});
		});
	}
	

}
