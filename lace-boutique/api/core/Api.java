package api.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.SessionFactory;

import api.endpoint.EndPoint;
import api.endpoint.endpoints.AccountEndPoint;
import api.endpoint.endpoints.AdminEndPoint;
import api.endpoint.endpoints.AuthToken;
import api.endpoint.endpoints.MiscEndpoint;
import api.endpoint.endpoints.ShopEndPoint;
import api.endpoint.endpoints.TestEndPoint;
import api.services.AccountService;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.HibernateSession;
import api.sql.hibernate.dao.ShopDAO;
import api.sql.hibernate.entities.Brand;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;

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
	private static int MAX_ROW_COUNT = 15;
	
	private static EndPoint[] endPoints = { 
			new TestEndPoint(), 
			new AuthToken(),
			new AccountEndPoint(),
			new ShopEndPoint(),
			new AdminEndPoint(),
			new MiscEndpoint()
			};
	private static String basePath = "/laceApi";

	public static void main(String[] args) {
		restContext = new RestContext(8080, basePath);
		initializeEndPoints();
		initializeHibernateSession();
		
		//AccountService accountService = new AccountService();
		
//		accountService.unsubscribeToNewsletter("alex_football_2k8@hotmail.co.uk");
		
//		accountService.beginPasswordReset("alex_football_2k8@hotmail.co.uk");
		
		//accountService.confirmPasswordReset("xVia43h-8iTITVHe-XCwpcTM", "Test123!");
		
		/*
		 * Test Cases
		 */
		//populateMockDatabase();
		
		//Order order = (Order) hibernateQuery.getObject(Order.class, 1);
		//List<OrderItem> orderItems = AccountDAO.getOrderItemsByOrder(order);
		
		//EmailService emailService = new EmailService();
		
//		System.out.println(orderItems.get(0).toString());
//		emailService.sendOrderConfirmedEmail(order, orderItems);
//		emailService.sendOrderUpdatedEmail(order);
		//emailService.sendOrderCancelledEmail(order);
		
//		emailService.sendNewAccountRegistrationEmail(account);
//		emailService.sendResetPasswordEmail(account, "o1mf9fnafnan-9fd9fid9fid-fdk9fka9k");
//		emailService.sendPasswordChangedEmail(account, "fiajdofjadojfiajfiodajfoiajdiofjaiofdaiojf");
		
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

	/**
	 * @return the mAX_ROW_COUNT
	 */
	public static int getMaxRowCount()
	{
		return MAX_ROW_COUNT;
	}

	/**
	 * @param mAX_ROW_COUNT the mAX_ROW_COUNT to set
	 */
	public void setMaxRowCount(int mAX_ROW_COUNT)
	{
		MAX_ROW_COUNT = mAX_ROW_COUNT;
	}

}