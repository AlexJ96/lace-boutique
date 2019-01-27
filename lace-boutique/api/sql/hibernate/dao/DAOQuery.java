package api.sql.hibernate.dao;

import org.hibernate.Session;

@FunctionalInterface
public interface DAOQuery {
	public Object query(Session session);
}
