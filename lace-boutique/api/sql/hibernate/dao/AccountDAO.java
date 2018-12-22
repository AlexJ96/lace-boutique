package api.sql.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Account;

public class AccountDAO {
	
	private static Session session = Api.getSessionFactory().openSession();

	@SuppressWarnings("unchecked")
	public static Account getAccountById(int id) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("id", id));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public static Account getAccountByEmail(String email) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("email", email));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public static Account getAccountByUsername(String username) {
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.eq("username", username));
		
		List<Account> accounts = criteria.list();
		return accounts.size() > 0 ? accounts.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public static List<Account> getAllAccounts() {
		Criteria criteria = session.createCriteria(Account.class);
		return criteria.list();
	}
}
