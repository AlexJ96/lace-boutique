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
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.CartItem;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemSpec;

public class CartDAO extends HibernateDAO {
	
	@Transactional
	@SuppressWarnings("unchecked")
	public static Cart getCartForAccount(Account account) {
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
	@Transactional
	public static Cart getNoneRegisteredCart(Cart cart) {
		DAOQuery query = (session) -> {
			List<CartItem> newCartItems = new ArrayList<CartItem>();
			
			for (CartItem cartItem : cart.getCartItems()) {
				int itemSpecId = cartItem.getItemSpec().getId();
				
				Criteria criteria = session.createCriteria(ItemSpec.class);
				criteria.add(Restrictions.eq("id", itemSpecId));
				
				List<ItemSpec> itemSpec = criteria.list();
				if (!itemSpec.isEmpty()) {
					cartItem.setItemSpec(itemSpec.get(0));
				}
				newCartItems.add(cartItem);
			}
			
			cart.setCartItems((ArrayList<CartItem>) newCartItems);
			return cart;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Cart)d.query(session, query);
	}
	
	@Transactional
	public static Cart removeFromCart(Cart cart, CartItem cartItem) {
		List<CartItem> cartItemList = cart.getCartItems();
		CartItem foundCartItem = null;
		
		for (CartItem cartItemFromList : cartItemList) {
		    if (cartItemFromList.getId() == cartItem.getId()) {
		    	foundCartItem = cartItemFromList;
		    }
		}
        
        if (foundCartItem != null) {
        	cartItemList.remove(foundCartItem);
        	cart.setCartItems((ArrayList<CartItem>) cartItemList);
        	if (cart.getId() != 0)
        		Api.getHibernateQuery().deleteObject(foundCartItem);
        } else {
        	System.out.println("Wishlist item not found.");
        }
        return cart;
	}
	
	public static CartItem addToCart(Cart cart, ItemSpec itemSpec, int quantity) {
		DAOQuery query = (session) -> {
			CartItem cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setItemSpec(itemSpec);
			cartItem.setQuantity(quantity);
			
			boolean alreadyExists = false;
			for (CartItem currentCartItem : cart.getCartItems()) {
				if (currentCartItem.getItemSpec().getId() == cartItem.getItemSpec().getId())
					alreadyExists = true;
			}
			
			if (cart.getId() != 0) {
				if (!alreadyExists)
					Api.getHibernateQuery().saveOrUpdateObject(cartItem);
			}
			

			int itemSpecId = cartItem.getItemSpec().getId();
			int itemIdCopy = cartItem.getItemSpec().getItem().getId();
			
			ItemSpec newItemSpec = new ItemSpec();
			newItemSpec.setId(itemSpecId);
			
			Item item = new Item();
			item.setId(itemIdCopy);
			newItemSpec.setItem(item);
			
			cartItem.setItemSpec(newItemSpec);
			
			return cartItem;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		CartItem cartItem = (CartItem) d.query(session, query);
		
		if (cartItem == null) {
			System.out.println("Cannot add item to wishlist.");
		}
		
		return cartItem;
	}
	
}
