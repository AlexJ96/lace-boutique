package api.entities;

import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Wishlist;

public class Token {
	
	private Account account;
	private Wishlist wishlist;
	
	public Token() {
		
	}
	
	public Token(Account account, Wishlist wishlist) {
		this.account = account;
		this.wishlist = wishlist;
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

	@Override
	public String toString() {
		return "Token [account=" + account + "]";
	}

}
