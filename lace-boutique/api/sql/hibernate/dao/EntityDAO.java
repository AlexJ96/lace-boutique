package api.sql.hibernate.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.*;

/**
 * EntityDAO is a hotfix for the stupid hibernate 
 * being incompetency to join without the object 
 * owning another entities directly with an ID. So,
 * this class is not intended to be used anywhere outside
 * entities.
 * 
 * @author Jason
 *
 */
public class EntityDAO extends HibernateDAO{
	public static List<ItemImage> getImageOf(ItemSpec itemSpec, boolean defaultImage){
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(ItemImage.class);
			criteria.add(Restrictions.eq("colour", itemSpec.getColour()));
			criteria.add(Restrictions.eq("item", itemSpec.getItem()));
			criteria.add(Restrictions.eq("defaultImage", defaultImage));
			
			List<ItemImage> itemImages = criteria.list();
			return itemImages;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		
		return (List<ItemImage>) d.query(session, query);
	}
}
