package api.services;

import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.MiscDAO;
import api.sql.hibernate.entities.MiscPages;

public class MiscService {
	
	private HibernateQuery hibernateQuery = new HibernateQuery();
	
	public MiscPages getMiscPageForUrl(String websiteUrl) {
		return MiscDAO.getMiscPageByUrl(websiteUrl.replace('"', ' ').trim());
	}
	
}
