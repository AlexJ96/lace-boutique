package api.sql.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
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
	        	return null;
	        }
	        return wishLists.get(0);
		};
		
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Wishlist)d.query(session, query);
	}
	
	@Transactional
	public static Wishlist removeFromWishlist(Wishlist wishlist, int wishlistItemId) {
		List<WishlistItem> wishlistItemList = wishlist.getWishlistItem();
		WishlistItem foundWishlistItem = null;
		
		for (WishlistItem wishlistItem : wishlistItemList) {
		    if (wishlistItem.getId() == wishlistItemId) {
		    	foundWishlistItem = wishlistItem;
		    }
		}
        
        if (foundWishlistItem != null) {
        	wishlistItemList.remove(foundWishlistItem);
        	wishlist.setWishlistItemList((ArrayList<WishlistItem>) wishlistItemList);
        	if (wishlist.getId() != 0)
        		Api.getHibernateQuery().deleteObject(foundWishlistItem);
        } else {
        	System.out.println("Wishlist item not found.");
        }
        return wishlist;
	}
	
	@SuppressWarnings("unchecked")
	public static WishlistItem addToWishlist(Wishlist wishlist, int itemId) {
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
			
			WishlistItem wishlistItem = new WishlistItem();
			wishlistItem.setWishlist(wishlist);
			wishlistItem.setItemSpec(itemSpec);
			
			boolean alreadyExists = false;
			for (WishlistItem currentWishlistItem : wishlist.getWishlistItem()) {
				if (currentWishlistItem.getItemSpec().getId() == wishlistItem.getItemSpec().getId())
					alreadyExists = true;
			}
			
			System.out.println(alreadyExists);
			
			if (wishlist.getId() != 0) {
				if (!alreadyExists)
					Api.getHibernateQuery().saveOrUpdateObject(wishlistItem);
			}
			

			int itemSpecId = wishlistItem.getItemSpec().getId();
			int itemIdCopy = wishlistItem.getItemSpec().getItem().getId();
			
			ItemSpec newItemSpec = new ItemSpec();
			newItemSpec.setId(itemSpecId);
			
			Item item = new Item();
			item.setId(itemIdCopy);
			newItemSpec.setItem(item);
			
			wishlistItem.setItemSpec(newItemSpec);
			
			return wishlistItem;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		WishlistItem wishlistItem = (WishlistItem) d.query(session, query);
		
		if (wishlistItem == null) {
			System.out.println("Cannot add item to wishlist.");
		}
		
		return wishlistItem;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addToWishlist(Account account, int itemId) {
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
			return false;
		}
		return true;
	}
}
