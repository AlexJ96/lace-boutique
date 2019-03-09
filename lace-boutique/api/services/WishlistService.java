package api.services;

import org.bson.types.ObjectId;

import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.WishlistDAO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Wishlist;
import api.utils.Responses;
import api.utils.Utils;

public class WishlistService {

	/**
	 * Removes item from wishlist
	 * @param account
	 * @return new token
	 */
	public String removeFromWishlist(Account account, int wishlistItemId) {
		WishlistDAO.removeFromWishlist(account, wishlistItemId);
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account);
    	return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Adds an item to wishlist
	 * @param account
	 * @param itemId
	 * @return new token
	 */
	public String addToWishlist(Account account, int itemId) {
		WishlistDAO.addToWishlist(account, itemId);
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account);
    	return Utils.getJsonBuilder().toJson(newToken);
	}
	
	
	/**
	 * Get's the wishlist for the given account
	 * @param account
	 * @return wishlist or null (if no wishlist)
	 */
	public Wishlist getWishlistForAccount(Account account) {
		Wishlist wishlist = WishlistDAO.getWishlistForAccount(account);
		if (wishlist != null)
			wishlist.setAccount(null);
		return wishlist;
	}
	
}
