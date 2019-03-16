package api.endpoint.endpoints;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.Stripe;
import com.stripe.model.Charge;

import api.endpoint.EndPoint;
import api.services.AccountService;
import api.services.CartService;
import api.services.WishlistService;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.utils.Utils;
import spark.Service;

public class AccountEndPoint implements EndPoint {
	private AccountService accountService = new AccountService();
	private WishlistService wishlistService = new WishlistService();
	private CartService cartService = new CartService();
	private Gson gson = new Gson();
	
	public AccountEndPoint() {
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
			
			spark.post("/address-load", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object, Account.class);
				
			    return Utils.getJsonBuilder().toJson(accountService.getAddressesForAccount(account));
			});
			
			spark.post("/wishlist-add", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				
				return Utils.getJsonBuilder().toJson(wishlistService.addToWishlist(account, wishlist, cart, object.get("ItemId").getAsInt()));
			});
			
			spark.post("/wishlist-remove", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				
				return Utils.getJsonBuilder().toJson(wishlistService.removeFromWishlist(account, wishlist, cart, object.get("WishlistItemId").getAsInt()));
			});
			
			spark.post("/wishlist", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				
				return Utils.getJsonBuilder().toJson(wishlistService.getAccountWishlist(account));
			});
			
			spark.post("/cart-add", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				ItemSpec itemSpec = gson.fromJson(object.get("ItemSpec"), ItemSpec.class);
				
				return Utils.getJsonBuilder().toJson(cartService.addToCart(account, wishlist, cart, itemSpec, object.get("Amount").getAsInt()));
			});
			
			spark.post("/cart-remove", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				CartItem cartItem = gson.fromJson(object.get("CartItem"), CartItem.class);
				
				return Utils.getJsonBuilder().toJson(cartService.removeFromCart(account, wishlist, cart, cartItem));
			});
			
			spark.post("/cart", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Account account = gson.fromJson(object.get("Account"), Account.class);
				
				return Utils.getJsonBuilder().toJson(cartService.getAccountCart(account));
			});
			
			spark.post("/cart-reload", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Cart cart = gson.fromJson(object.get("Cart"), Cart.class);
				
				return Utils.getJsonBuilder().toJson(cartService.getNoneRegisteredCart(cart));
			});
			
			spark.post("/confirm-order", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				JsonObject paymentObject = (JsonObject) object.get("paymentToken");
				JsonObject cardObject = (JsonObject) paymentObject.get("card");
				
				System.out.println(object);
				
				Stripe.apiKey = "sk_test_PmT0QU17uI9pLou57Acf5kzv";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", 999);
				params.put("currency", "gbp");
				params.put("source", "tok_visa");
				Charge charge = Charge.create(params);
				
				System.out.println(charge);
				
				return "";
			});
			
		});
	}
	
	

}
