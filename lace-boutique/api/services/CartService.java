package api.services;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import api.sql.hibernate.dao.CartDAO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.utils.Utils;

public class CartService {
	
	/**
	 * Adds an item to cart
	 * @param account
	 * @param itemId
	 * @return new token
	 */
	public String addToCart(Account account, Wishlist wishlist, Cart cart, ItemSpec itemSpec, int quantity) {
		CartItem cartItem = CartDAO.addToCart(cart, itemSpec, quantity);
		System.out.println(cartItem);
		String newToken;
		if (cart.getId() == 0) {
			boolean alreadyExists = false;
			for (CartItem currentCartItem : cart.getCartItems()) {
				if (currentCartItem.getItemSpec().getId() == cartItem.getItemSpec().getId())
					alreadyExists = true;
			}
			if (!alreadyExists)
				cart.getCartItems().add(cartItem);
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null, wishlist, cart);	
		} else {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
		}
		return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Removes the cart item id from the cart
	 * @param account
	 * @param wishlst
	 * @param cart
	 * @param cartItemId
	 * @return token
	 */
	public String removeFromCart(Account account, Wishlist wishlist, Cart cart, CartItem cartItem) {
		Cart updatedCart = CartDAO.removeFromCart(cart, cartItem);
		String newToken;
		if (cart.getId() == 0) {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null, wishlist, updatedCart);	
		} else {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
		}
		return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Returns the FULL cart object for the given account
	 * @param account
	 * @return cart
	 */
	public Cart getAccountCart(Account account) {
		Cart cart = CartDAO.getCartForAccount(account);
		if (cart != null) {
			cart.setAccount(null);
		} else {
			cart = new Cart();
			cart.setId(0);
			cart.setAccount(null);
			cart.setCartItems(new ArrayList<CartItem>());
		}
		return cart;
	}
	
	/**
	 * Refreshes the cart object with all details
	 * @param cart
	 * @return
	 */
	public Cart getNoneRegisteredCart(Cart cart) {
		return CartDAO.getNoneRegisteredCart(cart);
	}
	
	
	/**
	 * Get's the MODIFIED cart for the given account (Mainly for token)
	 * @param account
	 * @return cart
	 */
	public Cart getCartForAccount(Account account) {
		Cart cart = CartDAO.getCartForAccount(account);
		if (cart != null) {
			cart.setAccount(null);
			if (cart.getCartItems() == null) {
				cart.setCartItems(new ArrayList<CartItem>());
			} else {
				for (CartItem cartItem : cart.getCartItems()) {
					int itemSpecId = cartItem.getItemSpec().getId();
					int itemId = cartItem.getItemSpec().getItem().getId();
					
					ItemSpec itemSpec = new ItemSpec();
					itemSpec.setId(itemSpecId);
					
					Item item = new Item();
					item.setId(itemId);
					itemSpec.setItem(item);
					
					cartItem.setItemSpec(itemSpec);
				}
			}
		} else {
			cart = new Cart();
			cart.setId(0);
			cart.setAccount(null);
			cart.setCartItems(new ArrayList<CartItem>());
		}
		return cart;
	}
	
}
