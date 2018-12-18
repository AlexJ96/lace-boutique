package api.endpoint.endpoints;

import org.eclipse.jetty.util.StringUtil;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dto.AccountDTO;
import api.sql.hibernate.entities.Account;
import api.utils.SecureUtils;
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
			    
			    if(!email.equals(confirmedEmail)){
			    	return Utils.getJsonBuilder().toJson("Emails are not the same.");
			    }
			    
			    String password = Utils.getJsonFieldAsString(jobject, "password");
			    String confirmedPassword = Utils.getJsonFieldAsString(jobject, "confirmPassword");
			    
			    if(!password.equals(confirmedPassword)){
			    	return Utils.getJsonBuilder().toJson("Passwords are not the same.");
			    }
			    
			    String hashedPassword = SecureUtils.bCrypt10Password(password);
			    
			    String canEmail = Utils.getJsonFieldAsString(jobject, "subscription");
			    
			    Account account = new Account();
			    account.setEmail(email);
			    
			    account.setPassword(hashedPassword);
			    account.setCanEmail( StringUtil.isNotBlank(canEmail) );
			    
			    if(hibernateQuery.saveObject(account)){
			    	System.out.println("Success");
			    	return Utils.getJsonBuilder().toJson("Done");
			    }else{
			    	System.out.println("Failed");
			    	return Utils.getJsonBuilder().toJson("Cannot create a new account! Please try again later");
			    }
			});
			
			spark.post("/login", (request, response) -> {
				System.out.println("Logging in");
				String data = request.body();
				
				JsonElement jelement = new JsonParser().parse(data);
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String email = Utils.getJsonFieldAsString(jobject, "email");
			    String password = Utils.getJsonFieldAsString(jobject, "password");
				
			    Account account = AccountDTO.getAccountByEmail(email);
			    if(account == null){
			    	return Utils.getJsonBuilder().toJson("Invalid email or password");
			    }
			    String hashedPassword = SecureUtils.bCrypt10Password(password);
			    
			    if(BCrypt.checkpw(password, account.getPassword())){
			    	System.out.println("Login Success");
			    	return Utils.getJsonBuilder().toJson("Success");
			    }else{
			    	System.out.println("Login Failed");
			    	return Utils.getJsonBuilder().toJson("Invalid email or password");
			    }
			});
			
		});
	}
	
	

}
