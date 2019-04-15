package api.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.bson.types.ObjectId;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import api.endpoint.EndPoint;
import api.endpoint.endpoints.AccountEndPoint;
import api.endpoint.endpoints.AuthToken;
import api.endpoint.endpoints.ShopEndPoint;
import api.endpoint.endpoints.TestEndPoint;
import api.services.TokenService;
import api.services.WishlistService;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.HibernateSession;
import api.sql.hibernate.dao.DAOQuery;
import api.sql.hibernate.dao.ShopDAO;
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
		
		itemTest();
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
		String[] colours = { "Blue", "Black", "Pink", "Red" };
		String[] sizes = { "6", "8", "10", "12", "14", "16", "18" };
		String[] categories = { "Clothing", "Brands", "Dresses", "Accessories", "Footwear" };
		
		String[] clothingTypes = { "Tops", "Blouses", "Jumpsuits", "Playsuits", "Skirts",
	        					   "Shorts", "Loungewear", "Activewear", "Coats", "Jackets",
	        					   "Knitwear", "Jeans", "Trousers", "Swimwear" };

		String[] brands = { "Forever Unique", "Genese London", "Kevan Jon", "Access", "Blake Seven",
							"Comino Couture", "Freddy", "Guess", "Leo & Ugo", "Salsa", "Passioni",
							"Lindsey Brown", "Quay", "Silvian Heach", "Nadine Merabi", "Just Unique",
        					"Elisa Cavaletti" };
		
		String[] dressTypes = { "Maxi Dresses", "Occasion Dresses", "Prom Dresses", "Mini Dresses",
	        					"Skater Dresses", "Sequin Dresses", "Day Dresses", "Bridesmaid Dresses",
	        					"Wrap Dresses", "Bodycon Dresses", "Midi Dresses" };
		
		String[] accessoriesTypes = { "Bags", "Belts", "Scarves", "Sunglasses", "Socks" };
		
		String[] footwearTypes = { "Trainers", "Pumps", "Shoes", "Boots", "Slippers" };
		
		double[] prices = { 29.99, 12.99, 121.00, 64.99, 49.50, 200.00, 16.99, 34.99 };
		List<Colour> coloursArray = new ArrayList<Colour>();
		List<Size> sizesArray = new ArrayList<Size>();
		List<Brand> brandsArray = new ArrayList<Brand>();
		
		for (String colourString : colours) {
			Colour colour = new Colour();
			colour.setColour(colourString);
			coloursArray.add(ShopDAO.saveColour(colour));
		}
		
		for (String sizeString : sizes) {
			Size size = new Size();
			size.setSize(sizeString);
			sizesArray.add(ShopDAO.saveSize(size));
		}
		
		for (String brandString : brands) {
			Brand brand = new Brand();
			brand.setBrand(brandString);
			brandsArray.add(ShopDAO.saveBrand(brand));
		}

		Random r = new Random();
		
		for (String s : clothingTypes) {
			int random = r.nextInt((5 - 2) + 1) + 2;
			for (int i = 0; i < r.nextInt(random); i++) {
				Item item = new Item();
				item.setCategory("Clothing");
				item.setBrand(brandsArray.get(r.nextInt(brandsArray.size())));
				item.setType(s);
				item.setDescription(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setName(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setPrice(prices[r.nextInt(prices.length)]);
				
				item = ShopDAO.saveItem(item);
				
				for (int k = 0; k < coloursArray.size(); k++) {
					ItemImage itemImage = new ItemImage();
					itemImage.setItem(item);
					itemImage.setColour(coloursArray.get(k));
					itemImage.setDefaultImage(k == 0 ? true : false);
					itemImage.setUrl("https://static.bershka.net/4/photos2/2018/I/0/1/p/5038/969/428/5038969428_1_1_3.jpg");
					
					itemImage = ShopDAO.saveItemImage(itemImage);
					
					for (int j = 0; j < sizesArray.size(); j++) {
						ItemSpec itemSpec = new ItemSpec();
						itemSpec.setItem(item);
						itemSpec.setColour(coloursArray.get(k));
						itemSpec.setSize(sizesArray.get(j));
						itemSpec.setQuantity(r.nextInt(3));
						
						itemSpec = ShopDAO.saveItemSpec(itemSpec);
					}
				}
			}
		}
		
		for (String s : dressTypes) {
			int random = r.nextInt((5 - 2) + 1) + 2;
			for (int i = 0; i < r.nextInt(random); i++) {
				Item item = new Item();
				item.setCategory("Dresses");
				item.setBrand(brandsArray.get(r.nextInt(brandsArray.size())));
				item.setType(s);
				item.setDescription(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setName(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setPrice(prices[r.nextInt(prices.length)]);

				item = ShopDAO.saveItem(item);
				
				for (int k = 0; k < coloursArray.size(); k++) {
					ItemImage itemImage = new ItemImage();
					itemImage.setItem(item);
					itemImage.setColour(coloursArray.get(k));
					itemImage.setDefaultImage(k == 0 ? true : false);
					itemImage.setUrl("https://static.bershka.net/4/photos2/2018/I/0/1/p/5038/969/428/5038969428_1_1_3.jpg");
					
					itemImage = ShopDAO.saveItemImage(itemImage);
					
					for (int j = 0; j < sizesArray.size(); j++) {
						ItemSpec itemSpec = new ItemSpec();
						itemSpec.setItem(item);
						itemSpec.setColour(coloursArray.get(k));
						itemSpec.setSize(sizesArray.get(j));
						itemSpec.setQuantity(r.nextInt(3));
						
						itemSpec = ShopDAO.saveItemSpec(itemSpec);
					}
				}
			}
		}
		
		for (String s : accessoriesTypes) {
			int random = r.nextInt((10 - 5) + 1) + 2;
			for (int i = 0; i < r.nextInt(random); i++) {
				Item item = new Item();
				item.setCategory("Accessories");
				item.setBrand(brandsArray.get(r.nextInt(brandsArray.size())));
				item.setType(s);
				item.setDescription(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setName(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setPrice(prices[r.nextInt(prices.length)]);
				
				for (int k = 0; k < coloursArray.size(); k++) {
					ItemImage itemImage = new ItemImage();
					itemImage.setItem(item);
					itemImage.setColour(coloursArray.get(k));
					itemImage.setDefaultImage(k == 0 ? true : false);
					itemImage.setUrl("https://static.bershka.net/4/photos2/2018/I/0/1/p/5038/969/428/5038969428_1_1_3.jpg");
					
					itemImage = ShopDAO.saveItemImage(itemImage);
					
					for (int j = 0; j < sizesArray.size(); j++) {
						ItemSpec itemSpec = new ItemSpec();
						itemSpec.setItem(item);
						itemSpec.setColour(coloursArray.get(k));
						itemSpec.setSize(sizesArray.get(j));
						itemSpec.setQuantity(r.nextInt(3));
						
						itemSpec = ShopDAO.saveItemSpec(itemSpec);
					}
				}
			}
		}
		
		for (String s : footwearTypes) {
			int random = r.nextInt((10 - 5) + 1) + 2;
			for (int i = 0; i < r.nextInt(random); i++) {
				Item item = new Item();
				item.setCategory("Footwear");
				item.setBrand(brandsArray.get(r.nextInt(brandsArray.size())));
				item.setType(s);
				item.setDescription(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setName(item.getBrand().getBrand() + " " + s + " - " + i);
				item.setPrice(prices[r.nextInt(prices.length)]);

				item = ShopDAO.saveItem(item);
				
				for (int k = 0; k < coloursArray.size(); k++) {
					ItemImage itemImage = new ItemImage();
					itemImage.setItem(item);
					itemImage.setColour(coloursArray.get(k));
					itemImage.setDefaultImage(k == 0 ? true : false);
					itemImage.setUrl("https://static.bershka.net/4/photos2/2018/I/0/1/p/5038/969/428/5038969428_1_1_3.jpg");
					
					itemImage = ShopDAO.saveItemImage(itemImage);
					
					for (int j = 0; j < sizesArray.size(); j++) {
						ItemSpec itemSpec = new ItemSpec();
						itemSpec.setItem(item);
						itemSpec.setColour(coloursArray.get(k));
						itemSpec.setSize(sizesArray.get(j));
						itemSpec.setQuantity(r.nextInt(3));
						
						itemSpec = ShopDAO.saveItemSpec(itemSpec);
					}
				}
			}
		}
		
	}
			
	static void joinTest(){
		
	}
	
	
	static void itemTest(){
		List<ItemSpec> itemSpecList = ShopDAO.getSpecsForItem(1);
		System.out.println(itemSpecList.size());
		for (ItemSpec itemSpec : itemSpecList) {
			System.out.println("Associated Images: " + itemSpec.getItemImages().size());
			for(ItemImage ii : itemSpec.getItemImages()){
				System.out.println(
						"IS.Id: " + itemSpec.getId() + ", IS.Colour: " +itemSpec.getColour().getId() + ", IS.Item: " + itemSpec.getItem().getId() + ", "
								+ "II.Id: " + ii.getId() + ", II.Colour: " + ii.getColour().getId() + ", II.Item: " + ii.getItem().getId() );
			}
		}
	}

}