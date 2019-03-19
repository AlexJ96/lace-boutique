package api.sql.hibernate.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Address;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.OrderDetails;
import api.sql.hibernate.entities.OrderItem;

public class AccountDAO extends HibernateDAO{
	
//	private static Session session = Api.getSessionFactory().openSession();
	
	@SuppressWarnings("unchecked")
	public static Account getAccountById(int id) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Account.class);
			criteria.add(Restrictions.eq("id", id));
			
			List<Account> accounts = criteria.list();
			return accounts.size() > 0 ? accounts.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		
		return (Account) d.query(session, query);
		
	}
	
	@SuppressWarnings("unchecked")
	public static Account getAccountByEmail(String email) {	
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Account.class);
			criteria.add(Restrictions.eq("emailAddress", email));
			
			List<Account> accounts = criteria.list();
			return accounts.size() > 0 ? accounts.get(0) : null;			
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Account) d.query(session, query);
	}
	
	@Transactional
	public static Address saveNewAddress(Address address) {
		Api.getHibernateQuery().saveOrUpdateObject(address);
		return address;
	}
	
	@Transactional
	public static OrderDetails saveOrderDetails(OrderDetails orderDetails) {
		Api.getHibernateQuery().saveOrUpdateObject(orderDetails);
		return orderDetails;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public static List<api.sql.hibernate.entities.Order> getOrdersByAccount(Account account) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(api.sql.hibernate.entities.Order.class);
			criteria.createAlias("account", "account");
			criteria.add(Restrictions.eq("account.id", account.getId()));
			
			List<api.sql.hibernate.entities.Order> orders = criteria.list();
			return orders;		
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<api.sql.hibernate.entities.Order>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public static List<OrderItem> getOrderItemsByOrder(api.sql.hibernate.entities.Order order) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(OrderItem.class);
			criteria.createAlias("order", "order");
			criteria.add(Restrictions.eq("order.id", order.getId()));
			
			List<OrderItem> orderItems = criteria.list();
			return orderItems;		
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<OrderItem>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Account getAccountByUsername(String username) {
		DAOQuery query = (session) -> {
			Criteria criteria = session.createCriteria(Account.class);
			criteria.add(Restrictions.eq("username", username));
			
			List<Account> accounts = criteria.list();
			return accounts.size() > 0 ? accounts.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Account)d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Account> getAllAccounts() {
		
		DAOQuery query = (session) -> {
			Criteria criteria = session.createCriteria(Account.class);
			return criteria.list();
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Account>)d.query(session, query);
		
	}
	
	@SuppressWarnings("unchecked")
	public static List<Address> getAddressesForAccount(Account account) {
		DAOQuery query = (session) -> {
			Criteria criteria = session.createCriteria(Address.class);
			
			criteria.createAlias("account", "account");
			criteria.add(Restrictions.eq("account.id", account.getId()));
			
			List<Address> addresses = criteria.list();
			return addresses;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Address>)d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Cart getCart(Account account) {
		DAOQuery query = (session) -> {
			Criteria cartCriteria = session.createCriteria(Cart.class);
	
			cartCriteria.createAlias("account", "account");
			cartCriteria.add(Restrictions.eq("account.id", account.getId()));
			cartCriteria.addOrder(Order.desc("id"));
			
	        List<Cart> carts = cartCriteria.list();
	        if (carts.isEmpty()) {
	        	return null;
	        }
	        return carts.get(0);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Cart)d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static void removeFromCart(Account account, int cartItemId) {
				
		DAOQuery query = (session) -> {
			Criteria cartCriteria = session.createCriteria(CartItem.class);
	
			cartCriteria.createAlias("cart.account", "account");
			cartCriteria.add(Restrictions.eq("account.id", account.getId()));
			cartCriteria.add(Restrictions.eq("id", cartItemId));
			
	        List<CartItem> cartItems = cartCriteria.list();
	        return cartItems;
		};
		
		Session session = Api.getSessionFactory().getCurrentSession();
		List<CartItem> cartItems = (List<CartItem>)d.query(session, query);
		
        if (!cartItems.isEmpty()) {
        	System.out.println(cartItems.get(0).toString());
        	Api.getHibernateQuery().deleteObject(cartItems.get(0));
        }
	}
	
}
