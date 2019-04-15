package api.sql.hibernate.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.ProjectionsExtension;
import api.sql.hibernate.dto.DTOList;
import api.sql.hibernate.dto.FilterDTO;
import api.sql.hibernate.entities.Brand;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Discount;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
import api.sql.hibernate.entities.containers.ItemDetailsContainer;
import api.utils.StringUtils;

public class ShopDAO extends HibernateDAO {
	
	@Transactional
	public static Item saveItem(Item item) {
		DAOQuery query = (session)->{
			int id = (int) session.save(item);
			session.flush();
			return (Item) session.get(Item.class, id);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Item) d.query(session, query);
	}
	
	@Transactional
	public static ItemImage saveItemImage(ItemImage itemImage) {
		DAOQuery query = (session)->{
			int id = (int) session.save(itemImage);
			session.flush();
			return (ItemImage) session.get(ItemImage.class, id);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (ItemImage) d.query(session, query);
	}
	
	@Transactional
	public static ItemSpec saveItemSpec(ItemSpec itemSpec) {
		DAOQuery query = (session)->{
			session.saveOrUpdate(itemSpec);
			session.flush();
			return (ItemSpec) session.get(ItemSpec.class, itemSpec.getId());
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (ItemSpec) d.query(session, query);
	}
	
	@Transactional
	public static Colour saveColour(Colour colour) {
		DAOQuery query = (session)->{
			int id = (int) session.save(colour);
			session.flush();
			return (Colour) session.get(Colour.class, id);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Colour) d.query(session, query);
	}
	
	@Transactional
	public static Brand saveBrand(Brand brand) {
		DAOQuery query = (session)->{
			int id = (int) session.save(brand);
			session.flush();
			return (Brand) session.get(Brand.class, id);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Brand) d.query(session, query);
	}
	
	@Transactional
	public static Size saveSize(Size size) {
		DAOQuery query = (session)->{
			int id = (int) session.save(size);
			session.flush();
			return (Size) session.get(Size.class, id);
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Size) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static ItemImage getItemImageById(int itemImageId) {
		DAOQuery query = (session)-> {
			Criteria criteria = session.createCriteria(ItemImage.class);
			criteria.add(Restrictions.eq("id", itemImageId));
			
			List<ItemImage> itemImages = criteria.list();
			return itemImages.size() > 0 ? itemImages.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (ItemImage) d.query(session, query);
	}

	@SuppressWarnings("unchecked")
	public static List<Item> getAllItems(int pageNumber) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Item.class);

			int start = pageNumber * Api.getMaxRowCount();
			System.out.println(start);
			criteria.setMaxResults(Api.getMaxRowCount());
			criteria.setFirstResult(start);
			
			return criteria.list();
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Brand> getAllBrands() {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Brand.class);
			
			return criteria.list();
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Brand>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Colour> getAllColours() {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Colour.class);
			
			return criteria.list();
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Colour>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Brand getBrandById(int brandId) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Brand.class);
			criteria.add(Restrictions.eq("id", brandId));
			
			List<Brand> brandList = criteria.list();
			
			return brandList.size() > 0 ? brandList.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Brand) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Size> getAllSizes() {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Size.class);
			
			return criteria.list();
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Size>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static ItemDetailsContainer getAllDetailsForItem(int itemId) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Item.class);
			criteria.add(Restrictions.eq("id", itemId));
			List<Item> itemList = criteria.list();
			
			ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
			itemDetailsContainer.setItem(itemList.get(0));
			
			criteria = session.createCriteria(ItemImage.class);
			criteria.createAlias("item", "item");
			criteria.add(Restrictions.eq("item.id", itemId));
			List<ItemImage> itemImageList = criteria.list();

			itemDetailsContainer.setItemImage(itemImageList);
			
			criteria = session.createCriteria(ItemSpec.class);
			criteria.createAlias("item", "item");
			criteria.add(Restrictions.eq("item.id", itemId));
			List<ItemSpec> itemSpecList = criteria.list();

			itemDetailsContainer.setItemSpec(itemSpecList);
			
			return itemDetailsContainer;
			
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (ItemDetailsContainer) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> getAllItemsByCategory(String category) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Item.class);
			criteria.add(Restrictions.eq("category", category));
			
			List<Item> items = criteria.list();
			return items;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> getAllItemsByBrand(String brand) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Item.class);
			criteria.add(Restrictions.eq("brand", brand));
			
			List<Item> items = criteria.list();
			return items;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> filterItems(Map<String, String> filter) {
		DAOQuery query = (session)->{
			List<Item> brandAndCategory = filterItemTable(filter);
			
			List<ItemImage> colours = filterItemImageTable(filter);
			List<Item> coloursItem = new ArrayList<Item>();
			
			List<ItemSpec> sizes = filterItemSpecTable(filter);
			List<Item> sizesItem = new ArrayList<Item>();
			
			List<Item> filteredItems = new ArrayList<Item>();
			
			if (colours != null) {
				for (ItemImage itemImage : colours) {
					coloursItem.add(itemImage.getItem());
				}
			}
			
			if (sizes != null) {
				for (ItemSpec itemSpec : sizes) {
					sizesItem.add(itemSpec.getItem());
				}
			}
			
			if (colours == null && sizes == null && brandAndCategory == null) {
				return null;
			} else if (colours == null && sizes == null && brandAndCategory != null) {
				return brandAndCategory;
			} else if (colours == null && sizes != null && brandAndCategory == null) {
				return sizesItem;
			} else if (colours != null && sizes == null && brandAndCategory == null) {
				return coloursItem;
			} else if (colours != null && sizes == null && brandAndCategory != null) {
				for (Item item : brandAndCategory) {
					if (coloursItem.contains(item)) {
						filteredItems.add(item);
					}
				}
				return filteredItems;
			} else if (colours == null && sizes != null && brandAndCategory != null) {
				for (Item item : brandAndCategory) {
					if (sizesItem.contains(item)) {
						filteredItems.add(item);
					}
				}
				return filteredItems;
			} else if (colours != null && sizes != null && brandAndCategory == null) {
				for (Item item : coloursItem) {
					if (sizesItem.contains(item)) {
						filteredItems.add(item);
					}
				}
				return filteredItems;
			}
			
			for (Item item : brandAndCategory) {
				if (coloursItem.contains(item) && sizesItem.contains(item)) {
					filteredItems.add(item);
				}
			}
			
			return filteredItems;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemSpec> filterItemSpecTable(Map<String, String> filter) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(ItemSpec.class);
			String size = filter.containsKey("Size") ? filter.get("Size") : null;
			
			if (StringUtils.isNotBlank(size)) {
				criteria.add(Restrictions.eq("size", getSize(size)));
			} else {
				return null;
			}
			
			List<ItemSpec> itemSpecs = criteria.list();
			return itemSpecs;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<ItemSpec>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemImage> filterItemImageTable(Map<String, String> filter) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(ItemImage.class);
			String colour = filter.containsKey("Colour") ? filter.get("Colour") : null;
			
			if (StringUtils.isNotBlank(colour)) {
				criteria.add(Restrictions.eq("colour", getColour(colour)));
			} else {
				return null;
			}
			
			List<ItemImage> itemImages = criteria.list();
			return itemImages;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<ItemImage>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> filterItemTable(Map<String, String> filter) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Item.class);
			String brand = filter.containsKey("Brand") ? filter.get("Brand") : null;
			String category = filter.containsKey("Category") ? filter.get("Category") : null;
			String type = filter.containsKey("Type") ? filter.get("Type") : null;
			
			if (StringUtils.isNotBlank(brand)) {
				criteria.add(Restrictions.eq("brand", brand));
			}
			
			if (StringUtils.isNotBlank(category)) {
				criteria.add(Restrictions.eq("category", category));
			}
			
			if (StringUtils.isNotBlank(type)) {
				criteria.add(Restrictions.eq("type", type));
			}
			
			List<Item> items = criteria.list();
			return items.size() > 0 ? items : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	public static List<Item> getAllitemsByColour(String colourString) {
		DAOQuery query = (session)->{
			Colour colour = getColour(colourString);
			if (colour != null) {
				List<ItemImage> itemImages = getAllItemImagesByColour(colour);
				if (!itemImages.isEmpty()) {
					List<Item> items = new ArrayList<Item>();
					
					for (ItemImage itemImage : itemImages) {
						items.add(itemImage.getItem());
					}
					
					return items;
				}
				return null;
			}
			return null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<Item>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemSpec> getSpecsForItem(int itemId) {
		DAOQuery query = (session) -> {
			Criteria criteria = session.createCriteria(ItemSpec.class);
			criteria.createAlias("item", "item");
			criteria.add(Restrictions.eq("item.id", itemId));
			
			List<ItemSpec> itemSpecs = criteria.list();
			return itemSpecs;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<ItemSpec>) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Discount checkDiscountCode(String discountCode) {
		DAOQuery query = (session) -> {
			Criteria criteria = session.createCriteria(Discount.class);
			criteria.add(Restrictions.eq("discountCode", discountCode));
			
			List<Discount> discounts = criteria.list();
			return discounts.size() > 0 ? discounts.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Discount) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Colour getColour(String colour) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Colour.class);
			criteria.add(Restrictions.eq("colour", colour));
			
			List<Colour> colours = criteria.list();
			return colours.size() > 0 ? colours.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Colour) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static Size getSize(String size) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(Size.class);
			criteria.add(Restrictions.eq("size", size));
			
			List<Size> sizes = criteria.list();
			return sizes.size() > 0 ? sizes.get(0) : null;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Size) d.query(session, query);
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemImage> getAllItemImagesByColour(Colour colour) {
		DAOQuery query = (session)->{
			Criteria criteria = session.createCriteria(ItemImage.class);
			criteria.add(Restrictions.eq("colour", colour));
			
			List<ItemImage> itemImages = criteria.list();
			return itemImages;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<ItemImage>) d.query(session, query);
	}

	/**
	 * Getting a list of Item Image(with descriptions) filtered by category, size and colour.
	 * @param filters
	 * filters key only accept CATEGORY, SIZE AND COLOUR.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ItemImage> getItemImage(Map<String, List<String>> filters, int currentPage, int count, String order){
		
		DAOQuery query = (session)->{
			Set<String> keys = filters.keySet();
			for(String k : keys){
				if(!StringUtils.equals(k, "SIZE", "CATEGORY", "COLOUR", "BRAND", "TYPES")){
					return null;
				}
			}
			
			List<String> brand = filters.get("BRAND");
			List<Integer> brandIds = new ArrayList<Integer>();
			
			if (brand != null && !brand.isEmpty()) {
				Criteria brandCriteria = session.createCriteria(Brand.class);
				brandCriteria.add(Restrictions.in("brand", brand));
				brandCriteria.setProjection(Projections.property("id"));
				brandIds = brandCriteria.list();
			}
			
			Criteria criteria = session.createCriteria(ItemSpec.class);
			List<String> category = filters.get("CATEGORY");
			List<String> type = filters.get("TYPES");
			
			criteria.createAlias("item", "item");
			criteria.createAlias("size", "size");
			criteria.createAlias("colour", "colour");
			criteria.createAlias("item.brand", "brand");
			
			if (category != null && !category.isEmpty()) {
				criteria.add(Restrictions.eq("item.category", category.get(0)));
			}
			
			if (type != null && !type.isEmpty()) {
				criteria.add(Restrictions.in("item.type", type));
			}
			
			if (brandIds != null && !brandIds.isEmpty()) {
				criteria.add(Restrictions.in("brand.id", brandIds));
			}
			
			List<String> size = filters.get("SIZE");
			if(size != null && StringUtils.isNotBlank(size.toArray(new String[size.size()]))){
				criteria.add(Restrictions.in("size.size", size));
			}
			
			List<String> colour = filters.get("COLOUR");
			if(colour != null && StringUtils.isNotBlank(colour.toArray(new String[colour.size()]))){
				criteria.add(Restrictions.in("colour.colour", colour));
			}
			
	        criteria.setProjection(Projections.property("item.id"));
	        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	        
			int start = (currentPage - 1) * count;
	        
			List<Integer> itemIDs = criteria.list();
			if(itemIDs.isEmpty()){
				System.out.println("Items Null");
				return null;
			}
			
			criteria.setProjection(null);
			criteria.setProjection(Projections.property("colour.id"));
			List<Integer> colourIDs = criteria.list();
			if(colourIDs.isEmpty()){
				return null;
			}
			
			Criteria criteria2 = session.createCriteria(ItemImage.class);
			criteria2.createAlias("item", "item");
			criteria2.createAlias("colour", "colour");
			
			criteria2.add(Restrictions.in("colour.id", colourIDs));
			criteria2.add(Restrictions.in("item.id", itemIDs));
			
			List<String> isCategoryPage = filters.get("?NAMEIT?");  // <-- when you provide nothing,  you will have only the default images.
			if(isCategoryPage == null || isCategoryPage.isEmpty()){
				criteria2.add(Restrictions.eq("defaultImage", true));
			}
			
			criteria2.setFirstResult(start);
			criteria2.setMaxResults(count);
			
			if (order.equalsIgnoreCase("highest")) {
				criteria2.addOrder(Order.desc("item.price"));
			} else if (order.equalsIgnoreCase("lowest")) {
				criteria2.addOrder(Order.asc("item.price"));
			} else if (order.equalsIgnoreCase("newest")) {
				criteria2.addOrder(Order.desc("item.created"));
			} else if (order.equalsIgnoreCase("oldest")) {
				criteria2.addOrder(Order.asc("item.created"));
			}
			
			List<ItemImage> itemImage = criteria2.list();
			if(itemImage.isEmpty()){
				return null;
			}
			
			return itemImage;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (List<ItemImage>) d.query(session, query);
	}
	
	/**
	 * Use this method to display filter list on the website. However,  this method is not completed yet.
	 * TODO: implement query by colour.
	 * TODO: correlate query by colour and size.  As count should change together if a user has selected both size and colour.
	 * @param filters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,List<FilterDTO>> getFilters(Map<String, List<String>> filters){
		DAOQuery query = (session)->{
			Map<String, List<FilterDTO>> result = new HashMap();
			DTOList<FilterDTO> dtoList = new DTOList();
			
			Set<String> keys = filters.keySet();
			for(String k : keys){
				if(!StringUtils.equals(k, "CATEGORY", "SIZE", "COLOUR", "BRAND", "TYPES")){
					return null;
				}
			}
	
			List<String> brand = filters.get("BRAND");
			List<Integer> brandIds = new ArrayList<Integer>();
			
			if (brand != null && !brand.isEmpty()) {
				Criteria brandCriteria = session.createCriteria(Brand.class);
				brandCriteria.add(Restrictions.in("brand", brand));
				brandCriteria.setProjection(Projections.property("id"));
				brandIds = brandCriteria.list();
			}
			
			Criteria criteria = session.createCriteria(ItemSpec.class);
			List<String> category = filters.get("CATEGORY");
			List<String> type = filters.get("TYPES");
			
			criteria.createAlias("item", "item");
			criteria.createAlias("size", "size");
			criteria.createAlias("colour", "colour");
			criteria.createAlias("item.brand", "brand");
			
			if (category != null && !category.isEmpty()) {
				criteria.add(Restrictions.eq("item.category", category.get(0)));
			}
			
			if (type != null && !type.isEmpty()) {
				criteria.add(Restrictions.in("item.type", type));
			}
			
			List<String> sizes = filters.get("SIZE");
			if(sizes != null && !sizes.isEmpty()){
				criteria.add(Restrictions.in("size.size", sizes));
			}
			
			List<String> colours = filters.get("COLOUR");
			if(colours != null && !colours.isEmpty()){
				criteria.add(Restrictions.in("colour.colour", colours));
			}
			
			if (brandIds != null && !brandIds.isEmpty()) {
				criteria.add(Restrictions.in("brand.id", brandIds));
			}
			
			ProjectionList totalItemsCountProjection =Projections.projectionList();
			totalItemsCountProjection.add(ProjectionsExtension.countDistinct("item.id"));
			criteria.setProjection(totalItemsCountProjection);
	
			List<Long> totalCountResult = criteria.list();
			if(totalCountResult.isEmpty()){
				return null;
			}
			
			List<FilterDTO> totalFilters = new ArrayList<FilterDTO>();
			FilterDTO totalCountFilterDTO = new FilterDTO();
			totalCountFilterDTO.setKey("totalAmount");
			totalCountFilterDTO.setKeyCount(totalCountResult.get(0));
			totalFilters.add(totalCountFilterDTO);
			result.put("TOTAL_COUNT", totalFilters);
			
			ProjectionList p1=Projections.projectionList();
			p1.add(Projections.groupProperty("size.size"));
	        p1.add(ProjectionsExtension.countDistinct("item.id"));
			criteria.setProjection(p1);
	        
	        
			List<Object[]> sizeFilterResult = criteria.list();
			if(sizeFilterResult.isEmpty()){
				return null;
			}
			
			List<FilterDTO> sizeFilters = null;
			try {
				sizeFilters = dtoList.getDTOList(FilterDTO.class, sizeFilterResult);
				result.put("SIZE_FILTERS", sizeFilters);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			ProjectionList p2=Projections.projectionList();
			p2.add(Projections.groupProperty("colour.colour"));
			p2.add(ProjectionsExtension.countDistinct("item.id"));
			
			criteria.setProjection(null);
			criteria.setProjection(p2);
			
			List<Object[]> colourFilterResult = criteria.list();
			List<FilterDTO> colourFilters = null;
			try {
				colourFilters = dtoList.getDTOList(FilterDTO.class, colourFilterResult);
				result.put("COLOUR_FILTERS", colourFilters);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			ProjectionList p3=Projections.projectionList();
			p3.add(Projections.groupProperty("brand.brand"));
			p3.add(ProjectionsExtension.countDistinct("item.id"));
			
			criteria.setProjection(null);
			criteria.setProjection(p3);
			
			List<Object[]> brandFilterResult = criteria.list();
			List<FilterDTO> brandFilters = null;
			try {
				brandFilters = dtoList.getDTOList(FilterDTO.class, brandFilterResult);
				result.put("BRAND_FILTERS", brandFilters);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			ProjectionList p4=Projections.projectionList();
			p4.add(Projections.groupProperty("item.type"));
			p4.add(ProjectionsExtension.countDistinct("item.id"));
			
			criteria.setProjection(null);
			criteria.setProjection(p4);
			
			List<Object[]> typeFilterResult = criteria.list();
			List<FilterDTO> typeFilters = null;
			try {
				typeFilters = dtoList.getDTOList(FilterDTO.class, typeFilterResult);
				result.put("TYPE_FILTERS", typeFilters);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			return result;
		};
		Session session = Api.getSessionFactory().getCurrentSession();
		return (Map<String,List<FilterDTO>>) d.query(session, query);
	}
	
	
	
}
