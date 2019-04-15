package api.endpoint.endpoints;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.services.AccountService;
import api.services.CartService;
import api.services.OrderService;
import api.services.TokenService;
import api.services.WishlistService;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Address;
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
	private OrderService orderService = new OrderService();
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
			
			spark.post("/address-update", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Address address = gson.fromJson(object, Address.class);
				
			    return Utils.getJsonBuilder().toJson(accountService.saveOrUpdateAddress(address));
			});
			
			spark.post("/address-add", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();

				Account account = gson.fromJson(object.get("Account"), Account.class);
				Address address = gson.fromJson(object.get("Address"), Address.class);
				
				address.setAccount(account);
				
			    return Utils.getJsonBuilder().toJson(accountService.saveOrUpdateAddress(address));
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
			
			spark.post("/wishlist-reload", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				Wishlist wishlist = gson.fromJson(object.get("Wishlist"), Wishlist.class);
				
				return Utils.getJsonBuilder().toJson(wishlistService.getNoneRegisteredWishlist(wishlist));
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
				boolean success = orderService.confirmOrder(request.body());
				if (success) {
					JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
					Account account = gson.fromJson(object.get("account"), Account.class);
					
					String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
					return Utils.getJsonBuilder().toJson(newToken);
				}
				return Utils.getJsonBuilder().toJson(orderService.confirmOrder(request.body()));
			});
			
			spark.post("/orders", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				Account account = gson.fromJson(object, Account.class);
				
				return Utils.getJsonBuilder().toJson(accountService.getOrders(account));
			});
			
			spark.post("/reset-password", (request, response) -> {
				return Utils.getJsonBuilder().toJson(accountService.beginPasswordReset(request.body()));
			});
			
			spark.post("/confirm-password-reset", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				
				String resetCode = object.get("ResetCode").getAsString();
				String newPassword = object.get("NewPassword").getAsString();
				
				return Utils.getJsonBuilder().toJson(accountService.confirmPasswordReset(resetCode, newPassword));
			});
			
			spark.post("/subscribe-newsletter", (request, response) -> {
				return Utils.getJsonBuilder().toJson(accountService.subscribeToNewsletter(request.body()));
			});
			
			spark.post("/unsubscribe-newsletter", (request, response) -> {
				return Utils.getJsonBuilder().toJson(accountService.unsubscribeToNewsletter(request.body()));
			});
			
		});
	}
	
	

}
