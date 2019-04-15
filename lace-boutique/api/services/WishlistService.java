package api.services;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import api.sql.hibernate.dao.CartDAO;
import api.sql.hibernate.dao.WishlistDAO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;
import api.utils.Utils;

public class WishlistService {
	
	/**
	 * Adds an item to wishlist
	 * @param account
	 * @param itemId
	 * @return new token
	 */
	public String addToWishlist(Account account, Wishlist wishlist, Cart cart, int itemId) {
		WishlistItem wishlistItem = WishlistDAO.addToWishlist(wishlist, itemId);
		String newToken;
		if (wishlist.getId() == 0) {
			boolean alreadyExists = false;
			for (WishlistItem currentWishlistItem : wishlist.getWishlistItem()) {
				if (currentWishlistItem.getItemSpec().getId() == wishlistItem.getItemSpec().getId())
					alreadyExists = true;
			}
			if (!alreadyExists)
				wishlist.getWishlistItem().add(wishlistItem);
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null, wishlist, cart);	
		} else {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
		}
		return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Removes the wishlist item id from the Wishlist
	 * @param account
	 * @param wishlist
	 * @param wishlistItemId
	 * @return token
	 */
	public String removeFromWishlist(Account account, Wishlist wishlist, Cart cart, int wishlistItemId) {
		Wishlist updatedWishlist = WishlistDAO.removeFromWishlist(wishlist, wishlistItemId);
		String newToken;
		if (wishlist.getId() == 0) {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null, updatedWishlist, cart);	
		} else {
			newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
		}
		return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Returns the FULL wishlist object for the given account
	 * @param account
	 * @return wishlist
	 */
	public Wishlist getAccountWishlist(Account account) {
		Wishlist wishlist = WishlistDAO.getWishlistForAccount(account);
		if (wishlist != null) {
			wishlist.setAccount(null);
		} else {
			wishlist = new Wishlist();
			wishlist.setId(0);
			wishlist.setAccount(null);
			wishlist.setWishlistItemList(new ArrayList<WishlistItem>());
		}
		return wishlist;
	}
	
	/**
	 * Refreshes the cart object with all details
	 * @param cart
	 * @return
	 */
	public Wishlist getNoneRegisteredWishlist(Wishlist wishlist) {
		return WishlistDAO.getNoneRegisteredWishlist(wishlist);
	}
	
	
	/**
	 * Get's the MODIFIED wishlist for the given account (Mainly for token)
	 * @param account
	 * @return wishlist
	 */
	public Wishlist getWishlistForAccount(Account account) {
		Wishlist wishlist = WishlistDAO.getWishlistForAccount(account);
		if (wishlist != null) {
			wishlist.setAccount(null);
			if (wishlist.getWishlistItem() == null) {
				wishlist.setWishlistItemList(new ArrayList<WishlistItem>());
			} else {
				for (WishlistItem wishlistItem : wishlist.getWishlistItem()) {
					int itemSpecId = wishlistItem.getItemSpec().getId();
					int itemId = wishlistItem.getItemSpec().getItem().getId();
					
					ItemSpec itemSpec = new ItemSpec();
					itemSpec.setId(itemSpecId);
					
					Item item = new Item();
					item.setId(itemId);
					itemSpec.setItem(item);
					
					wishlistItem.setItemSpec(itemSpec);
				}
			}
		} else {
			wishlist = new Wishlist();
			wishlist.setId(0);
			wishlist.setAccount(null);
			wishlist.setWishlistItemList(new ArrayList<WishlistItem>());
		}
		return wishlist;
	}
	
}
