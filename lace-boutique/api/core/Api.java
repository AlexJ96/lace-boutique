package api.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.hibernate.SessionFactory;

import api.endpoint.EndPoint;
import api.endpoint.endpoints.AccountEndPoint;
import api.endpoint.endpoints.AuthToken;
import api.endpoint.endpoints.ShopEndPoint;
import api.endpoint.endpoints.TestEndPoint;
import api.services.TokenService;
import api.services.WishlistService;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.HibernateSession;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Brand;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
import api.sql.hibernate.entities.Wishlist;

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
		
		Account account = new Account();
		account.setId(25);
		account.setTitle("Mr");
		account.setFirstName("Testing");
		account.setLastName("Testing");
		account.setEmailAddress("main@test.com");
		account.setGender("Male");
		account.setDateOfBirth(new GregorianCalendar(1996, 4, 28));
		account.setPassword("Test123!");
		account.setSalesInfo("Email");
		account.setNewStuffInfo("Email");
		account.setNewsletter(true);
		account.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		account.setCreatedFrom("IP");
		account.setLastLogged(new Timestamp(System.currentTimeMillis()));
		account.setLoggedFrom("IP");
		
		//String token = TokenService.generateToken(new ObjectId().toString(), 1L, account);
		//System.out.println(token);
		
		WishlistService wishlistService = new WishlistService();
		Wishlist wishlist = wishlistService.getWishlistForAccount(account);
		//System.out.println(wishlist.getWishlistItem().get(0));
		
		//token = wishlistService.addToWishlist(account, 1);
		
		//token = wishlistService.removeFromWishlist(account, wishlist.getWishlistItem().get(0).getId());
		//System.out.println(token);
		
//		hibernateQuery.saveOrUpdateObject(account);
		
		
		/*
		 * Test Cases
		 */
		//populateMockDatabase();
		//new EmailService().testEmail();
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
	
	private static void populateMockDatabase() {
		int itemAmount = 50;
		String[] colours = { "Blue", "Black", "Pink", "Yellow", "Green", "Orange", "Red" };
		String[] sizes = { "6", "8", "10", "12", "14", "16", "18" };
		String[] categories = { "Dresses", "Jeans", "Tops" };
		String[] brands = { "Dress Brand", "Jean Brand", "Top Brand" };
		double[] prices = { 29.99, 16.99, 34.99 };
		List<Colour> coloursArray = new ArrayList<Colour>();
		List<Size> sizesArray = new ArrayList<Size>();
		List<Brand> brandsArray = new ArrayList<Brand>();
		
		for (String colourString : colours) {
			Colour colour = new Colour();
			colour.setColour(colourString);
			coloursArray.add(colour);
			getHibernateQuery().saveOrUpdateObject(colour);
		}
		
		for (String sizeString : sizes) {
			Size size = new Size();
			size.setSize(sizeString);
			sizesArray.add(size);
			getHibernateQuery().saveOrUpdateObject(size);
		}
		
		for (String brandString : brands) {
			Brand brand = new Brand();
			brand.setBrand(brandString);
			brandsArray.add(brand);
			getHibernateQuery().saveOrUpdateObject(brand);
		}

		Random r = new Random();
		for (int i = 0; i < itemAmount; i++) {
			int randomNumber = r.nextInt(3);
			Item item = new Item();
			item.setCategory(categories[randomNumber]);
			item.setBrand(brandsArray.get(randomNumber));
			item.setDescription(item.getBrand() + " " + item.getCategory() + " " + i + " - Lovely!");
			item.setName(item.getBrand() + " " + item.getCategory() + " " + i);
			item.setPrice(prices[randomNumber]);
			
			getHibernateQuery().saveOrUpdateObject(item);
			
			for (int k = 0; k < coloursArray.size(); k++) {
				ItemImage itemImage = new ItemImage();
				itemImage.setItem(item);
				itemImage.setColour(coloursArray.get(k));
				itemImage.setDefaultImage(k == 0 ? true : false);
				itemImage.setUrl("www." + item.getName() + "-" + k + ".co.uk");
				
				getHibernateQuery().saveOrUpdateObject(itemImage);
				
				for (int j = 0; j < sizesArray.size(); j++) {
					ItemSpec itemSpec = new ItemSpec();
					itemSpec.setItem(item);
					itemSpec.setColour(coloursArray.get(k));
					itemSpec.setSize(sizesArray.get(j));
					itemSpec.setQuantity(r.nextInt(200));
					
					getHibernateQuery().saveOrUpdateObject(itemSpec);
				}
			}
		}
	}
			
	static void joinTest(){
		
	}

}