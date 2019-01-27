package api.sql.hibernate.dao;

import org.hibernate.Session;

@FunctionalInterface
public interface DAO {
	public Object query(Session session, DAOQuery query);
}
