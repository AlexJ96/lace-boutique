package api.sql.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;
import api.sql.hibernate.entities.containers.WishlistItemContainer;

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
	public static List<WishlistItemContainer> getWishlistItems(Account account) {
		Criteria wishlistCriteria = session.createCriteria(Wishlist.class);

		wishlistCriteria.createAlias("account", "account");
		wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
		wishlistCriteria.setProjection(Projections.property("id"));
        
        List<Integer> wishlistIdList = wishlistCriteria.list();
        if (wishlistIdList.isEmpty()) {
        	return null;
        }
        
        int wishlistId = wishlistIdList.get(0);
        
        Criteria wishlistItemCriteria = session.createCriteria(WishlistItem.class);
        wishlistItemCriteria.createAlias("wishlist", "wishlist");
        wishlistItemCriteria.createAlias("itemSpec", "itemSpec");
        wishlistItemCriteria.add(Restrictions.eq("wishlist.id", wishlistId));
        wishlistItemCriteria.setProjection(Projections.property("itemSpec"));
        
        List<ItemSpec> itemSpecs = wishlistItemCriteria.list();
        if (itemSpecs.isEmpty())
        	return null;
        
        List<Integer> colourIDs = new ArrayList<Integer>();
        List<Integer> itemIDs = new ArrayList<Integer>();
        for (ItemSpec itemSpec : itemSpecs) {
        	itemIDs.add(itemSpec.getItem().getId());
        	colourIDs.add(itemSpec.getColour().getId());
        }
        
        Criteria itemImageCriteria = session.createCriteria(ItemImage.class);
        itemImageCriteria.createAlias("item", "item");
        itemImageCriteria.createAlias("colour", "colour");
        
        Criterion[] criterion = new Criterion[itemIDs.size()];
        
        for (int i = 0; i < itemIDs.size(); i++) {
        	Criterion criteria = Restrictions.and(Restrictions.eq("item.id", (int)itemIDs.get(i)), Restrictions.eq("colour.id", (int)colourIDs.get(i)));
        	criterion[i] = criteria;
        }
        
        itemImageCriteria.add(Restrictions.or(criterion));
		
		List<ItemImage> itemImages = itemImageCriteria.list();
		List<WishlistItemContainer> wishlistItems = new ArrayList<WishlistItemContainer>();
		for (ItemImage itemImage : itemImages) {
			ItemSpec itemSpec = itemSpecs.stream().filter((spec) -> (spec.getColour().getId() == itemImage.getColour().getId() && spec.getItem().getId() == itemImage.getItem().getId())).findFirst().orElse(new ItemSpec());
			WishlistItemContainer wishlistItemContainer = new WishlistItemContainer(itemSpec.getSize(), itemImage);
			wishlistItems.add(wishlistItemContainer);
		}
		return wishlistItems.isEmpty() ? null : wishlistItems;
	}
}
