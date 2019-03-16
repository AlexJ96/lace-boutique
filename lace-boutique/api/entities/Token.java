package api.entities;

import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.Wishlist;

public class Token {
	
	private Account account;
	private Wishlist wishlist;
	private Cart cart;
	
	public Token() {
		
	}
	
	public Token(Account account, Wishlist wishlist, Cart cart) {
		this.account = account;
		this.wishlist = wishlist;
		this.cart = cart;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Wishlist getWishlist() {
		return wishlist;
	}
	
	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "Token [account=" + account + ", wishlist=" + wishlist + ", cart=" + cart + "]";
	}

}
