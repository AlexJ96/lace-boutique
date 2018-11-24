package api.core;

import java.util.Date;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.lept.*;
import org.bytedeco.javacpp.tesseract.*;
import org.hibernate.SessionFactory;

import api.endpoint.EndPoint;
import api.endpoint.endpoints.AuthToken;
import api.endpoint.endpoints.TestEndPoint;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.HibernateSession;
import api.sql.hibernate.entities.User;

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
	
	private static EndPoint[] endPoints = { new TestEndPoint(), new AuthToken() };
	private static String basePath = "/laceApi";

	public static void main(String[] args) {
		restContext = new RestContext(8080, basePath);
		initializeEndPoints();
		initializeHibernateSession();
		//testAddUser();
		//testGetUser();
		//testDeleteUser();
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
	
	public static void testAddUser() {
		User user = new User();
		user.setUsername("Editor 0");
		user.setCreatedBy("Administrator");
		user.setCreatedDate(new Date());

		hibernateQuery.saveObject(user);
	}
	
	public static void testGetUser() {
		User user = (User) hibernateQuery.getObject(new User(), 0);
		if (user != null) 
			System.out.println(user.getUsername());
		else
			System.out.println("User is null");
	}
	
	public static void testDeleteUser() {
		User user = (User) hibernateQuery.getObject(new User(), 0);
		if (user != null)
			hibernateQuery.deleteObject(user);
		else 
			System.out.println("User is null");
	}

}