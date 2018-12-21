package api.core;

import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.SessionFactory;

import api.auth.Authenticator;
import api.auth.TokenInfo;
import api.endpoint.EndPoint;
import api.endpoint.endpoints.AccountEndPoint;
import api.endpoint.endpoints.AuthToken;
import api.endpoint.endpoints.ShopEndPoint;
import api.endpoint.endpoints.TestEndPoint;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.HibernateSession;
import api.sql.hibernate.dto.AccountDTO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.WishlistItem;
import api.utils.SecureUtils;

/**
 * Api Initializer - Sets up all API Endpoints
 * @author Alex.Johnson
 *
 */
public class Api {
	
	private static RestContext restContext;
	
	private static HibernateSession hibernateSession;
	private static SessionFactory sessionFactory;
	private static HibernateQuery hibernateQuery;
	
	private static EndPoint[] endPoints = { 
			new TestEndPoint(), 
			new AuthToken(),
			new AccountEndPoint(),
			new ShopEndPoint()
			};
	private static String basePath = "/laceApi";

	public static void main(String[] args) {
		restContext = new RestContext(8080, basePath);
		initializeEndPoints();
		initializeHibernateSession();
		//testAddUser();
		//testGetUser();
		//testDeleteUser();
		//testGetSearch();
		//testPasswordValidator();
		//testAccountToken();
	}
	
	private static void initializeEndPoints() {
		restContext.enableCors();
		for (EndPoint endPoint : endPoints) {
			restContext.addEndpoint(endPoint);
		}
	}
	
	private static void initializeHibernateSession() {
		hibernateSession = new HibernateSession();
		sessionFactory = hibernateSession.buildSessionFactory();
		hibernateQuery = new HibernateQuery();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static HibernateQuery getHibernateQuery() {
		return hibernateQuery;
	}
	
	private static void testGetSearch() {
//		Account account = AccountDTO.getAccountByUsername("AlexJ");
		List<Account> accounts = AccountDTO.getAllAccounts();
		System.out.println(accounts.size());
	}
	
	public static void testAddUser() {
		Account account = new Account();
		account.setUsername("AlexJ");
		account.setEmail("email@email.com");
		account.setCanEmail(true);
		account.setPassword("password");
		
		hibernateQuery.saveObject(account);
		
		Wishlist wishList = new Wishlist();
		wishList.setAccount(account);
		
		hibernateQuery.saveObject(wishList);
		
		Item item = new Item();
		item.setName("Petite Dress");
		item.setDescription("A Dress designed for petite ladies");
		item.setPrice(49.99);
		item.setBrand("Designer Dresses");
		item.setCategory("Dresses");
		
		hibernateQuery.saveObject(item);
		
		ItemSpec itemSpec = new ItemSpec();
		itemSpec.setItem(item);
		itemSpec.setQuantity(100);
		itemSpec.setSize(null);//new Size()
		
		hibernateQuery.saveObject(itemSpec);
		
		ItemImage itemImage = new ItemImage();
		itemImage.setItem(item);
		itemImage.setColour(null);//new Colour()
		itemImage.setUrl("www.google.co.uk");
		
		hibernateQuery.saveObject(itemImage);
		
		WishlistItem wishItem = new WishlistItem();
		wishItem.setWishlist(wishList);
		wishItem.setItem(item);
		
		hibernateQuery.saveObject(wishItem);
	}
	
	public static void testGetUser() {
		Account account = (Account) hibernateQuery.getObject(Account.class, 1);
		System.out.println(account.toString());

		Wishlist wishlist = (Wishlist) hibernateQuery.getObject(Wishlist.class, account.getId());
		System.out.println(wishlist.toString());

		WishlistItem wishItem = (WishlistItem) hibernateQuery.getObject(WishlistItem.class, wishlist.getId());
		System.out.println(wishItem.toString());
		
		Item item = wishItem.getItem();
		System.out.println(item.toString());
		
		ItemSpec itemSpec = (ItemSpec) hibernateQuery.getObject(ItemSpec.class, item.getId());
		System.out.println(itemSpec.toString());
		
		ItemImage itemImage = (ItemImage) hibernateQuery.getObject(ItemImage.class, item.getId());
		System.out.println(itemImage.toString());
	}
	
	public static void testDeleteUser() {
		/*User user = (User) hibernateQuery.getObject(new User(), 0);
		if (user != null)
			hibernateQuery.deleteObject(user);
		else 
			System.out.println("User is null");*/
	}
	
	public static void testPasswordValidator(){
		System.out.println(SecureUtils.validatePassword("Test with your own password lol") ? "Valid" : "Invalid");
		System.out.println(SecureUtils.validatePassword("123") ? "Valid" : "Invalid");
		System.out.println(SecureUtils.validatePassword("12345678") ? "Valid" : "Invalid");
		System.out.println(SecureUtils.validatePassword("12312312123123121231231212312312") ? "Valid" : "Invalid");
	}
	
	public static void testAccountToken() {
		Account account = (Account) hibernateQuery.getObject(Account.class, 1);
		String token = Authenticator.generateWebToken(new ObjectId().toString(), 1L);
		System.out.println(token);
		TokenInfo tokenInfo = Authenticator.verifyToken(token);
		if (tokenInfo.getAccount() != null) {
			System.out.println(tokenInfo.getAccount().toString());
		}
	}

}