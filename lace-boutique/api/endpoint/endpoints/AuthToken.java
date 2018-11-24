package api.endpoint.endpoints;

import org.bson.types.ObjectId;

import api.auth.Authenticator;
import api.endpoint.EndPoint;
import api.utils.Utils;
import spark.Service;

public class AuthToken implements EndPoint {
	
	public AuthToken() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/token", () -> {
			spark.get("/request-token", (request, response) -> {
				return Utils.getJsonBuilder().toJson(Authenticator.generateWebToken(new ObjectId().toString(), 1L));
			});
		});
	}
	

}
