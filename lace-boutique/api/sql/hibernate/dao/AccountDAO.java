package api.sql.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.Wishlist;

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
}
