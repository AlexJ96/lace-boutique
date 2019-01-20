package api.sql.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;
import api.sql.hibernate.entities.containers.ItemContainer;

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
	public static List<Wishlist> getWishlists(Account account) {
		Criteria wishlistCriteria = session.createCriteria(Wishlist.class);

		wishlistCriteria.createAlias("account", "account");
		wishlistCriteria.add(Restrictions.eq("account.id", account.getId()));
        
        List<Wishlist> wishLists = wishlistCriteria.list();
        if (wishLists.isEmpty()) {
        	return null;
        }
        return wishLists;
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
}
