package api.sql.hibernate.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;

public class WishlistDAO extends HibernateDAO {
	
	@Transactional
	@SuppressWarnings("unchecked")
	public static Wishlist getWishlistForAccount(Account account) {
		DAOQuery query = (session) -> {
			Criteria wishlistCriteria = session.createCriteria(Wishlist.class);
	
			wishlistCriteria.createAlias("account", "account");
			wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
			wishlistCriteria.addOrder(Order.desc("id"));
	        
	        List<Wishlist> wishLists = wishlistCriteria.list();
	        if (wishLists.isEmpty()) {
	        	return new Wishlist();
	        }
	        return wishLists.get(0);
		};
		
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Wishlist)d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public static void removeFromWishlist(Account account, int wishlistId) {
		
		DAOQuery query = (session) -> {
			Criteria wishlistCriteria = session.createCriteria(WishlistItem.class);
	
			wishlistCriteria.createAlias("wishlist.account", "account");
			wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
			wishlistCriteria.add(Restrictions.eq("id", wishlistId));
			
	        return wishlistCriteria.list();
		};
        Session session = Api.getSessionFactory().getCurrentSession();
        List<WishlistItem> wishlistItems = (List<WishlistItem>)d.query(session, query);
        
        if (!wishlistItems.isEmpty()) {
        	System.out.println(wishlistItems.get(0).toString());
        	Api.getHibernateQuery().deleteObject(wishlistItems.get(0));
        } else {
        	System.out.println("Wishlist Items is empty");
        }
	}
	
	@SuppressWarnings("unchecked")
	public static void addToWishlist(Account account, int itemId) {
		DAOQuery query = (session) -> {
			Criteria itemImageCriteria = session.createCriteria(ItemImage.class);
	
			itemImageCriteria.createAlias("item", "item");
			itemImageCriteria.add(Restrictions.eq("item.id", Integer.valueOf(itemId)));
			itemImageCriteria.add(Restrictions.eq("defaultImage", true));
			
			List<ItemImage> itemImageList = itemImageCriteria.list();
			
			if (itemImageList.isEmpty()) {
				System.out.println("ItemImageList is empty");
				return null;
			}
			
			ItemImage itemImage = itemImageList.get(0);
			Colour colour = itemImage.getColour();
			
			Criteria itemSpecCriteria = session.createCriteria(ItemSpec.class);
			
			itemSpecCriteria.createAlias("item", "item");
			itemSpecCriteria.createAlias("colour", "colour");
			itemSpecCriteria.add(Restrictions.eq("item.id", Integer.valueOf(itemId)));
			itemSpecCriteria.add(Restrictions.eq("colour.id", colour.getId()));
			
			List<ItemSpec> itemSpecList = itemSpecCriteria.list();
			
			if (itemSpecList.isEmpty()) {
				System.out.println("ItemSpecList is empty");
				return null;
			}
			ItemSpec itemSpec = itemSpecList.get(0);
			
			Criteria wishlistCriteria = session.createCriteria(Wishlist.class);
			
			wishlistCriteria.createAlias("account", "account");
			wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
			
			List<Wishlist> wishlistList = wishlistCriteria.list();
			if (wishlistList.isEmpty()) {
				System.out.println("WishList is empty");
				return null;
			}
			Wishlist wishlist = wishlistList.get(0);
			
			WishlistItem wishlistItem = new WishlistItem();
			wishlistItem.setWishlist(wishlist);
			wishlistItem.setItemSpec(itemSpec);
			
			Api.getHibernateQuery().saveOrUpdateObject(wishlistItem);
			return wishlistItem;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		WishlistItem wishlistItem = (WishlistItem) d.query(session, query);
		
		if (wishlistItem == null) {
			System.out.println("Cannot add item to wishlist.");
		}		
	}
}
