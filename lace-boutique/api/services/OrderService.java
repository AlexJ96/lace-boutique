package api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.StringUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.Stripe;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.AccountDAO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Address;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.Order;
import api.sql.hibernate.entities.OrderDetails;
import api.sql.hibernate.entities.OrderItem;
import api.sql.hibernate.entities.OrderStatus;
import api.utils.Utils;

public class OrderService {
	
	private String STRIPE_API_KEY = "sk_test_PmT0QU17uI9pLou57Acf5kzv";
	private Gson gson = new Gson();
	private HibernateQuery hibernateQuery = new HibernateQuery();
	
	public boolean confirmOrder(String requestBody) {
		JsonObject object = new JsonParser().parse(requestBody).getAsJsonObject();
		JsonObject paymentObject = (JsonObject) object.get("paymentToken");
		JsonObject accountObject = (JsonObject) object.get("account");
		JsonObject addressObject = (JsonObject) object.get("address");
		
		String accountId = Utils.getJsonFieldAsString(accountObject, "id");
		Account account = null;
		
		OrderDetails orderDetails = null;
		
		String addressId = Utils.getJsonFieldAsString(addressObject, "id");
		Address address = null;
		
		if (!accountId.equalsIgnoreCase("0")) {
			account = gson.fromJson(object.get("account"), Account.class);
			account.setPhoneNumber(null);
		} else {
			orderDetails = new OrderDetails();
			orderDetails.setFirstName(Utils.getJsonFieldAsString(accountObject, "firstName"));
			orderDetails.setLastName(Utils.getJsonFieldAsString(accountObject, "lastName"));
			orderDetails.setEmailAddress(Utils.getJsonFieldAsString(accountObject, "emailAddress"));
			orderDetails = AccountDAO.saveOrderDetails(orderDetails);
		}
		
		if (StringUtil.isNotBlank(addressId)) {
			address = gson.fromJson(object.get("address"), Address.class);
		} else {
			address = new Address();
			address.setNumberStreet(Utils.getJsonFieldAsString(addressObject, "numberStreet"));
			address.setTown(Utils.getJsonFieldAsString(addressObject, "town"));
			address.setCity(Utils.getJsonFieldAsString(addressObject, "city"));
			address.setCountry(Utils.getJsonFieldAsString(addressObject, "country"));
			address.setPostcode(Utils.getJsonFieldAsString(addressObject, "postcode"));
			address = AccountDAO.saveNewAddress(address);
		}
		
		System.out.println(requestBody);

		Cart cart = gson.fromJson(object.get("cart"), Cart.class);
	    String totalPrice = Utils.getJsonFieldAsString(object, "totalPrice");
	    String orderType = Utils.getJsonFieldAsString(object, "deliveryType");
	    String postageType = Utils.getJsonFieldAsString(object, "postageType");
		
		Charge charge = createCharge(paymentObject.get("id").toString().replace('"', ' ').trim());
		
		Order order = new Order();
		if (orderDetails != null) 
			order.setOrderDetails(orderDetails);
		if (account != null)
			order.setAccount(account);
		order.setAddress(address);
		order.setOrderTotal(Double.valueOf(totalPrice));
		order.setDeliveryMethod(postageType);
		order.setOrderType(orderType);
		order.setOrderStatus(getDefaultOrderStatus());
		order.setPaymentToken(paymentObject.get("id").toString().replace('"', ' ').trim());
		order.setChargeId(charge.getId());
		
		if (!saveOrder(order)) {
			return false;
		}
		
		if (!completeOrder(order, cart)) {
			return false;
		}
		
		return true;
	}
	
	private boolean completeOrder(Order order, Cart cart) {
		List<CartItem> cartItems = cart.getCartItems();
		
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setItemSpec(cartItem.getItemSpec());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setOrder(order);
			if (!saveOrderItem(orderItem)) {
				return false;
			}
			deleteCartItem(cartItem);
		}
		return true;
	}
	
	private void deleteCartItem(CartItem cartItem) {
		hibernateQuery.deleteObject(cartItem);
	}
	
	private boolean saveOrderItem(OrderItem orderItem) {
		return hibernateQuery.saveOrUpdateObject(orderItem);
	}
	
	private boolean saveAddress(Address address) {
		return hibernateQuery.saveOrUpdateObject(address);
	}
	
	private boolean saveOrder(Order order) {
		return hibernateQuery.saveOrUpdateObject(order);
	}
	
	private OrderStatus getDefaultOrderStatus() {
		return (OrderStatus) hibernateQuery.getObject(OrderStatus.class, 1);
	}
	
	private Charge createCharge(String paymentSourceId) {
		Stripe.apiKey = STRIPE_API_KEY;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", 999);
		params.put("currency", "gbp");
		params.put("source", "tok_visa");
		Charge charge = null;
		try {
			charge = Charge.create(params);
		} catch (CardException e) {
		  // Since it's a decline, CardException will be caught
		  System.out.println("Status is: " + e.getCode());
		  System.out.println("Message is: " + e.getMessage());
		} catch (RateLimitException e) {
		  // Too many requests made to the API too quickly
		} catch (InvalidRequestException e) {
		  // Invalid parameters were supplied to Stripe's API
		} catch (AuthenticationException e) {
		  // Authentication with Stripe's API failed
		  // (maybe you changed API keys recently)
		} catch (ApiException e) {
		  // Network communication with Stripe failed
		} catch (StripeException e) {
		  // Display a very generic error to the user, and maybe send
		  // yourself an email
		} catch (Exception e) {
		  // Something else happened, completely unrelated to Stripe
		}
		return charge;
	}

}
