package api.endpoint.endpoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.sql.hibernate.entities.Account;
import api.utils.Utils;
import spark.Service;

public class AccountEndPoint implements EndPoint {
	
	public AccountEndPoint() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/account", () -> {
			spark.post("/register", (request, response) -> {
				System.out.println(request.body());
				
				String data = request.body();
				
				JsonElement jelement = new JsonParser().parse(data);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String email = jobject.get("email").getAsString();
			    String confirmedEmail = jobject.get("email").getAsString();
			    
			    String password = jobject.get("password").getAsString();
			    String confirmedPassword = jobject.get("password").getAsString();
			    
			    System.out.println(jobject.get("email").getAsString());
			    
			    Account account = new Account();
			    account.setEmail(email);
			    account.setPassword(password);

				return Utils.getJsonBuilder().toJson("Test Response Body");
			});
		});
	}
	

}
