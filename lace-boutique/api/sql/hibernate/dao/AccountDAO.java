package api.sql.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;

public class AccountDAO {
	
	private static Session session = Api.getSessionFactory().openSession();

	@SuppressWarnings("unchecked")
	public static Account getAccountById(int id) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("id", id));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public static Account getAccountByEmail(String email) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("email", email));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public static Account getAccountByUsername(String username) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("username", username));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public static List<Account> getAllAccounts() {
		Criteria criteria = session.createCriteria(Account.class);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public static Wishlist getWishlists(Account account) {
		session.flush();
		Criteria wishlistCriteria = session.createCriteria(Wishlist.class);

		wishlistCriteria.createAlias("account", "account");
		wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
		wishlistCriteria.addOrder(Order.desc("id"));
        
        List<Wishlist> wishLists = wishlistCriteria.list();
        if (wishLists.isEmpty()) {
        	return null;
        }
        return wishLists.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static Cart getCart(Account account) {
		Criteria cartCriteria = session.createCriteria(Cart.class);

		cartCriteria.createAlias("account", "account");
		cartCriteria.add(Restrictions.eq("account.id", account.getId()));
		cartCriteria.addOrder(Order.desc("id"));
		
        List<Cart> carts = cartCriteria.list();
        if (carts.isEmpty()) {
        	return null;
        }
        return carts.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static void removeFromCart(Account account, int cartItemId) {
		Criteria cartCriteria = session.createCriteria(CartItem.class);

		cartCriteria.createAlias("cart.account", "account");
		cartCriteria.add(Restrictions.eq("account.id", account.getId()));
		cartCriteria.add(Restrictions.eq("id", cartItemId));
		
        List<CartItem> cartItems = cartCriteria.list();
        if (!cartItems.isEmpty()) {
        	System.out.println(cartItems.get(0).toString());
        	Api.getHibernateQuery().deleteObject(cartItems.get(0));
        }
	}
	
	
	
	public static void removeFromWishlist(Account account, int wishlistId) {
		Criteria wishlistCriteria = session.createCriteria(WishlistItem.class);

		wishlistCriteria.createAlias("wishlist.account", "account");
		wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
		wishlistCriteria.add(Restrictions.eq("id", wishlistId));
		
        List<WishlistItem> wishlistItems = wishlistCriteria.list();
        if (!wishlistItems.isEmpty()) {
        	System.out.println(wishlistItems.get(0).toString());
        	Api.getHibernateQuery().deleteObject(wishlistItems.get(0));
        } else {
        	System.out.println("Wishlist Items is empty");
        }
	}
	
	public static void addToWishlist(Account account, int itemId) {
		/**
		 * ItemImage
		 */
		Criteria itemImageCriteria = session.createCriteria(ItemImage.class);

		itemImageCriteria.createAlias("item", "item");
		itemImageCriteria.add(Restrictions.eq("item.id", Integer.valueOf(itemId)));
		itemImageCriteria.add(Restrictions.eq("defaultImage", true));
		
		List<ItemImage> itemImageList = itemImageCriteria.list();
		if (itemImageList.isEmpty()) {
			System.out.println("ItemImageList is empty");
			return;
		}
		
		ItemImage itemImage = itemImageList.get(0);
		Colour colour = itemImage.getColour();
		
		/**
		 * ItemSpec
		 */
		Criteria itemSpecCriteria = session.createCriteria(ItemSpec.class);
		
		itemSpecCriteria.createAlias("item", "item");
		itemSpecCriteria.createAlias("colour", "colour");
		itemSpecCriteria.add(Restrictions.eq("item.id", Integer.valueOf(itemId)));
		itemSpecCriteria.add(Restrictions.eq("colour.id", colour.getId()));
		
		List<ItemSpec> itemSpecList = itemSpecCriteria.list();
		if (itemSpecList.isEmpty()) {
			System.out.println("ItemSpecList is empty");
			return;
		}
		ItemSpec itemSpec = itemSpecList.get(0);
		
		/**
		 * Wishlist
		 */
		Criteria wishlistCriteria = session.createCriteria(Wishlist.class);
		
		wishlistCriteria.createAlias("account", "account");
		wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
		
		List<Wishlist> wishlistList = wishlistCriteria.list();
		if (wishlistList.isEmpty()) {
			System.out.println("WishList is empty");
			return;
		}
		Wishlist wishlist = wishlistList.get(0);
		
		WishlistItem wishlistItem = new WishlistItem();
		wishlistItem.setWishlist(wishlist);
		wishlistItem.setItemSpec(itemSpec);
		
		Api.getHibernateQuery().saveObject(wishlistItem);
	}
}
