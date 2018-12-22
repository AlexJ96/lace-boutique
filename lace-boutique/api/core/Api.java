package api.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import api.sql.hibernate.dto.ShopDTO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
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
		testAddUser();
		//testGetUser();
		//testDeleteUser();
		//testGetSearch();
		//testPasswordValidator();
		//testAccountToken();
		
//		testShopDTOWithOnlyCategory();
		
		List<String> oneSize = new ArrayList();
		oneSize.add("S"); // @ALEXJ  Change this to XS and you will see the difference
		List<String> twoSize = new ArrayList();
		twoSize.add("XS");
		twoSize.add("S");
		
		List<String> oneColour = new ArrayList();
		oneColour.add("Blue");
		
		List<String> twoColour = new ArrayList();
		twoColour.add("Blue");
		twoColour.add("Black");
		
//		testShopDTOWithOnlyCategoryAndSize(oneSize);
//		testShopDTOWithOnlyCategoryAndSize(twoSize);
//		testShopDTOWithOnlyCategoryAndColour(oneColour);
//		testShopDTOWithOnlyCategoryAndColour(twoColour);
//		
//		testShopDTOWithCategoryAndColourAndSize(oneSize, oneColour);
//		testShopDTOWithCategoryAndColourAndSize(oneSize, twoColour);
//		testShopDTOWithCategoryAndColourAndSize(twoSize, oneColour);
//		testShopDTOWithCategoryAndColourAndSize(twoSize, twoColour);
		
		testShopDTOGetItemImageWithOnlyCategory();
		
		testShopDTOGetItemImageWithOnlyCategoryAndSize(oneSize);
		testShopDTOGetItemImageWithOnlyCategoryAndSize(twoSize);
		testShopDTOGetItemImageWithOnlyCategoryAndColour(oneColour);
		testShopDTOGetItemImageWithOnlyCategoryAndColour(twoColour);
		
		testShopDTOGetItemImageWithCategoryAndColourAndSize(oneSize, oneColour);
		testShopDTOGetItemImageWithCategoryAndColourAndSize(oneSize, twoColour);
		testShopDTOGetItemImageWithCategoryAndColourAndSize(twoSize, oneColour);
		testShopDTOGetItemImageWithCategoryAndColourAndSize(twoSize, twoColour);
		
		
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
		itemSpec.setSize((Size) hibernateQuery.getObject(Size.class, 1));
		itemSpec.setColour((Colour) hibernateQuery.getObject(Colour.class, 1));
		
		hibernateQuery.saveObject(itemSpec);
		
		ItemImage itemImage = new ItemImage();
		itemImage.setColour((Colour) hibernateQuery.getObject(Colour.class, 1));
		itemImage.setItem(item);
		itemImage.setDefaultImage(true);
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
	
	
	
	
	public static void testShopDTOGetItemImageWithOnlyCategory(){
		System.out.println("Category Test Only");
		Map<String, List<String>> filters = new HashMap();
		List<String> category = new ArrayList();
		category.add("Dresses");
		filters.put("CATEGORY", category);
		List<ItemImage> images = ShopDTO.getItemImage(filters);
		for(ItemImage i : images){
			System.out.println(i);
		}
	}
	
	public static void testShopDTOWGetItemImageithOnlyCategory(){
		System.out.println("Category Test Only");
		Map<String, List<String>> filters = new HashMap();
		List<String> category = new ArrayList();
		category.add("Dresses");
		filters.put("CATEGORY", category);
		List<ItemImage> images = ShopDTO.getItemImage(filters);
		for(ItemImage i : images){
			System.out.println(i);
		}
	}
	
	public static void testShopDTOGetItemImageWithOnlyCategoryAndSize(List<String> size){
		System.out.println("1 Category with "+ size.size() +" size(s) Test Only");
		Map<String, List<String>> filters = new HashMap();
		List<String> category = new ArrayList();
		category.add("Dresses");
		filters.put("CATEGORY", category);
		
		filters.put("SIZE", size);
		
		List<ItemImage> images = ShopDTO.getItemImage(filters);
		for(ItemImage i : images){
			System.out.println(i);
		}
	}
	
	public static void testShopDTOGetItemImageWithOnlyCategoryAndColour(List<String> colour){
		System.out.println("1 Category with "+ colour.size() +" colour(s) Test Only");
		Map<String, List<String>> filters = new HashMap();
		List<String> category = new ArrayList();
		category.add("Dresses");
		filters.put("CATEGORY", category);
		
		filters.put("COLOUR", colour);
		
		List<ItemImage> images = ShopDTO.getItemImage(filters);
		for(ItemImage i : images){
			System.out.println(i);
		}
	}
	
	public static void testShopDTOGetItemImageWithCategoryAndColourAndSize(List<String> size, List<String> colour){
		System.out.println("1 Category with "+ size.size() +" size(s)" + colour.size() +" colour(s) Test.");
		Map<String, List<String>> filters = new HashMap();
		List<String> category = new ArrayList();
		category.add("Dresses");
		filters.put("CATEGORY", category);
		filters.put("COLOUR", colour);
		filters.put("SIZE", size);
		
		List<ItemImage> images = ShopDTO.getItemImage(filters);
		for(ItemImage i : images){
			System.out.println(i);
		}
	}

}