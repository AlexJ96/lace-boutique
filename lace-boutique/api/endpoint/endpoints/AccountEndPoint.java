package api.endpoint.endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.services.AccountService;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.entities.Account;
import api.utils.Utils;
import spark.Service;

public class AccountEndPoint implements EndPoint {
	
	private HibernateQuery hibernateQuery;
	private AccountService accountService = new AccountService();
	private Gson gson = new Gson();
	
	public AccountEndPoint() {
		hibernateQuery = new HibernateQuery();
	}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/account", () -> {
			
			spark.post("/register", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object, Account.class);
				
			    return Utils.getJsonBuilder().toJson(accountService.createAccount(account));
			});
			
			spark.post("/login", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
			    
			    String username = Utils.getJsonFieldAsString(object, "Username");
			    String password = Utils.getJsonFieldAsString(object, "Password");
			    
			    return Utils.getJsonBuilder().toJson(accountService.attemptLogin(username, password));
			});
			
			spark.post("/logout", (request, response) -> {
			    return Utils.getJsonBuilder().toJson(accountService.attemptLogout());
			});
			
			spark.post("/update", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object, Account.class);
				
			    return Utils.getJsonBuilder().toJson(accountService.updateAccount(account));
			});
			
			spark.post("/removeCartItem", (request, response) -> {
				//TODO Remove Cart Item
				return "";
			});
			
			spark.post("/addWishlistItem", (request, response) -> {
				//TODO Add Wishlist Item
				return "";
			});
			
			spark.post("/removeWishlistItem", (request, response) -> {
				//TODO Remove Wishlist Item
				return "";
			});
			
		});
	}
	
	

}
