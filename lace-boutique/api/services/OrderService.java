package api.services;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

public class OrderService {
	
	private String STRIPE_API_KEY = "sk_test_PmT0QU17uI9pLou57Acf5kzv";
	
	public boolean confirmOrder() {
		return true;
	}
	
	
	private Charge createCharge() {
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
