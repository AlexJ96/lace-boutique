package api.endpoint.endpoints;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.services.TokenService;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.Wishlist;
import api.utils.Utils;
import spark.Service;

public class AuthToken implements EndPoint {

	private Gson gson = new Gson();
	
	public AuthToken() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/token", () -> {
			spark.get("/request-token", (request, response) -> {
				return Utils.getJsonBuilder().toJson(TokenService.generateToken(new ObjectId().toString(), 7L, null, null, null));
			});
			
			spark.post("/refresh-token", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				
				return Utils.getJsonBuilder().toJson(TokenService.generateToken(new ObjectId().toString(), 7L, account, wishlist, cart));
			});
		});
	}
	

}
