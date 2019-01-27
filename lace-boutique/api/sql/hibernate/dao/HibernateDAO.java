package api.sql.hibernate.dao;

import org.hibernate.Transaction;

public class HibernateDAO {
	
	public static DAO d = (session, query) -> {
		Transaction tx = session.beginTransaction();
		Object result = null;
		try{
			result = query.query(session);
			tx.commit();
		}catch(Exception ex){
			System.out.println(ex);
			tx.rollback();
		}
		return result;
	};
	
}
