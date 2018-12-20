package api.endpoint.endpoints;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.auth.Authenticator;
import api.endpoint.EndPoint;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dto.AccountDTO;
import api.sql.hibernate.entities.Account;
import api.utils.Responses;
import api.utils.SecureUtils;
import api.utils.StringUtils;
import api.utils.Utils;
import spark.Service;

public class AccountEndPoint implements EndPoint {
	
	private HibernateQuery hibernateQuery;
	public AccountEndPoint() {
		hibernateQuery = new HibernateQuery();
	}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/account", () -> {
			
			spark.post("/register", (request, response) -> {
				System.out.println(request.body());
				
				String data = request.body();
				
				JsonElement jelement = new JsonParser().parse(data);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String email = Utils.getJsonFieldAsString(jobject, "email");
			    String confirmedEmail = Utils.getJsonFieldAsString(jobject, "confirmEmail");
			    
			    if(StringUtils.isBlank(email, confirmedEmail)){
			    	return Utils.getJsonBuilder().toJson(Responses.INVALID_EMAIL.getResponse());
			    }
			    
			    if(!email.equals(confirmedEmail)){
			    	return Utils.getJsonBuilder().toJson(Responses.EMAILS_DO_NOT_MATCH.getResponse());
			    }
			    
			    String password = Utils.getJsonFieldAsString(jobject, "password");
			    String confirmedPassword = Utils.getJsonFieldAsString(jobject, "confirmPassword");
			    
			    if(StringUtils.isBlank(password, confirmedPassword)){
			    	return Utils.getJsonBuilder().toJson(Responses.INVALID_PASSWORD.getResponse());
			    }
			    
			    if(!SecureUtils.validatePassword(password)){
			    	return Utils.getJsonBuilder().toJson(Responses.INSECURE_PASSWORD.getResponse());
			    }
			    
			    if(!password.equals(confirmedPassword)){
			    	return Utils.getJsonBuilder().toJson(Responses.PASSWORDS_DO_NOT_MATCH.getResponse());
			    }
			    
			    String hashedPassword = SecureUtils.bCrypt10Password(password);
			    
			    String canEmail = Utils.getJsonFieldAsString(jobject, "subscription");
			    
			    Account account = new Account();
			    account.setEmail(email);
			    
			    account.setPassword(hashedPassword);
			    account.setCanEmail( StringUtils.isNotBlank(canEmail) );
			    
			    if(hibernateQuery.saveObject(account)){
			    	System.out.println("Success");
			    	return Utils.getJsonBuilder().toJson("Done");
			    }else{
			    	System.out.println("Failed");
			    	return Utils.getJsonBuilder().toJson(Responses.FAILURE_CREATING_ACCOUNT.getResponse());
			    }
			});
			
			spark.post("/login", (request, response) -> {
				System.out.println("Logging in");
				String data = request.body();
				
				JsonElement jelement = new JsonParser().parse(data);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String email = Utils.getJsonFieldAsString(jobject, "email");
			    String password = Utils.getJsonFieldAsString(jobject, "password");
				
			    if(StringUtils.isBlank(email, password)){
			    	return Utils.getJsonBuilder().toJson(Responses.MISSING_EMAIL_OR_PASSWORD.getResponse());
			    }
			    
			    Account account = AccountDTO.getAccountByEmail(email);
			    if(account == null){
			    	return Utils.getJsonBuilder().toJson(Responses.INVAID_EMAIL_OR_PASSWORD.getResponse());
			    }
			    
			    if(BCrypt.checkpw(password, account.getPassword())){
			    	System.out.println("Login Success");
			    	String newToken = "Token: " + Authenticator.generateWebToken(new ObjectId().toString(), 1L, account);
			    	return Utils.getJsonBuilder().toJson(newToken);
			    }else{
			    	System.out.println("Login Failed");
			    	return Utils.getJsonBuilder().toJson(Responses.INVAID_EMAIL_OR_PASSWORD.getResponse());
			    }
			});
			
		});
	}
	
	

}
