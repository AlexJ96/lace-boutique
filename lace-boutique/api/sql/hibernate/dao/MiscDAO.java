package api.sql.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.MiscPages;

public class MiscDAO extends HibernateDAO{
	
	@SuppressWarnings("unchecked")
	public static MiscPages getMiscPageByUrl(String websiteUrl) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(MiscPages.class);
			criteria.add(Restrictions.eq("websiteUrl", websiteUrl));
			
			List<MiscPages> miscPages = criteria.list();
			return miscPages.size() > 0 ? miscPages.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		
		return (MiscPages) d.query(session, query);	
	}
	
}
