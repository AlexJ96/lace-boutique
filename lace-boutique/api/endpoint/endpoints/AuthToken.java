package api.endpoint.endpoints;

import org.bson.types.ObjectId;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.auth.Authenticator;
import api.core.Api;
import api.endpoint.EndPoint;
import api.sql.hibernate.entities.Account;
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
			
			spark.post("/refresh-token", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();
			    String token = Utils.getJsonFieldAsString(jobject, "Token");
				return Utils.getJsonBuilder().toJson(Authenticator.refreshToken(token));
			});
		});
	}
	

}
