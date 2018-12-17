package api.sql.hibernate;

import org.hibernate.Session;
import api.core.Api;

public class HibernateQuery {
	
	private Session session;
	
	private void openSession() {
		session = Api.getSessionFactory().openSession();
	}
	
	private void beginTransaction() {
		session.beginTransaction();
	}
	
	private void commitTransaction() {
		session.getTransaction().commit();
	}
	
	private void rollBackTransaction() {
		session.getTransaction().rollback();
	}
	
	private void closeSession() {
		session.close();
	}
	
	public void saveObject(Object object) {
		try {
			openSession();
			beginTransaction();
			
			session.save(object);
			
			commitTransaction();
		} catch (Exception e) {
			rollBackTransaction();
			System.out.println(e.getMessage());
		} finally {
			closeSession();
		}
	}	
	
	public void saveOrUpdateObject(Object object) {
		try {
			openSession();
			beginTransaction();
			
			session.saveOrUpdate(object);
			
			commitTransaction();
		} catch (Exception e) {
			rollBackTransaction();
			System.out.println(e.getMessage());
		} finally {
			closeSession();
		}
	}
	
	@SuppressWarnings("finally")
	public Object getObject(Object object, Integer id) {
		Object returnObject = null;
		try {
			openSession();
			beginTransaction();
			
			returnObject = session.get(object.getClass(), id);
			
			commitTransaction();
		} catch (Exception e) {
			rollBackTransaction();
			System.out.println(e.getMessage());
			return null;
		} finally {
			closeSession();
			return returnObject;
		}
	}
	
	public void deleteObject(Object object) {
		try {
			openSession();
			beginTransaction();
			
			session.delete(object);
			
			commitTransaction();			
		} catch (Exception e) {
			rollBackTransaction();
			System.out.println(e.getMessage());
		} finally {
			closeSession();
		}
	}
}
