package api.sql.hibernate.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CountProjection;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.SessionStatistics;

import api.core.Api;
import api.sql.hibernate.ProjectionsExtension;
import api.sql.hibernate.SumProjection;
import api.sql.hibernate.dto.DTOList;
import api.sql.hibernate.dto.FilterDTO;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
import api.utils.StringUtils;

public class ShopDAO {
	
	private static Session session = Api.getSessionFactory().openSession();

	@SuppressWarnings("unchecked")
	public static List<Item> getAllItems() {
		Criteria criteria = session.createCriteria(Item.class);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> getAllItemsByCategory(String category) {
		Criteria criteria = session.createCriteria(Item.class);
		criteria.add(Restrictions.eq("category", category));
		
		List<Item> items = criteria.list();
		return items;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> getAllItemsByBrand(String brand) {
		Criteria criteria = session.createCriteria(Item.class);
		criteria.add(Restrictions.eq("brand", brand));
		
		List<Item> items = criteria.list();
		return items;
	}
	
	public static List<Item> filterItems(Map<String, String> filter) {
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
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemSpec> filterItemSpecTable(Map<String, String> filter) {
		Criteria criteria = session.createCriteria(ItemSpec.class);
		String size = filter.containsKey("Size") ? filter.get("Size") : null;
		
		if (StringUtils.isNotBlank(size)) {
			criteria.add(Restrictions.eq("size", getSize(size)));
		} else {
			return null;
		}
		
		List<ItemSpec> itemSpecs = criteria.list();
		return itemSpecs;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemImage> filterItemImageTable(Map<String, String> filter) {
		Criteria criteria = session.createCriteria(ItemImage.class);
		String colour = filter.containsKey("Colour") ? filter.get("Colour") : null;
		
		if (StringUtils.isNotBlank(colour)) {
			criteria.add(Restrictions.eq("colour", getColour(colour)));
		} else {
			return null;
		}
		
		List<ItemImage> itemImages = criteria.list();
		return itemImages;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Item> filterItemTable(Map<String, String> filter) {
		Criteria criteria = session.createCriteria(Item.class);
		String brand = filter.containsKey("Brand") ? filter.get("Brand") : null;
		String category = filter.containsKey("Category") ? filter.get("Category") : null;
		
		if (StringUtils.isNotBlank(brand)) {
			criteria.add(Restrictions.eq("brand", brand));
		}
		
		if (StringUtils.isNotBlank(category)) {
			criteria.add(Restrictions.eq("category", category));
		}
		
		List<Item> items = criteria.list();
		return items.size() > 0 ? items : null;
	}
	
	public static List<Item> getAllitemsByColour(String colourString) {
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
	}
	
	@SuppressWarnings("unchecked")
	public static Colour getColour(String colour) {
		Criteria criteria = session.createCriteria(Colour.class);
		criteria.add(Restrictions.eq("colour", colour));
		
		List<Colour> colours = criteria.list();
		return colours.size() > 0 ? colours.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public static Size getSize(String size) {
		Criteria criteria = session.createCriteria(Size.class);
		criteria.add(Restrictions.eq("size", size));
		
		List<Size> sizes = criteria.list();
		return sizes.size() > 0 ? sizes.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ItemImage> getAllItemImagesByColour(Colour colour) {
		Criteria criteria = session.createCriteria(ItemImage.class);
		criteria.add(Restrictions.eq("colour", colour));
		
		List<ItemImage> itemImages = criteria.list();
		return itemImages;
	}
	
	

	
	/**
	 * Getting a list of Item Image(with descriptions) filtered by category, size and colour.
	 * @param filters
	 * filters key only accept CATEGORY, SIZE AND COLOUR.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ItemImage> getItemImage(Map<String, List<String>> filters, int currentPage, int count){
		Set<String> keys = filters.keySet();
		for(String k : keys){
			if(!StringUtils.equals(k, "SIZE", "CATEGORY", "COLOUR")){
				return null;
			}
		}
		
		Criteria criteria = session.createCriteria(ItemSpec.class);
		List<String> category = filters.get("CATEGORY");
		
		criteria.createAlias("item", "item");
		criteria.createAlias("size", "size");
		criteria.createAlias("colour", "colour");
		
		criteria.add(Restrictions.eq("item.category", category.get(0)));
		
		// Query by size
		List<String> size = filters.get("SIZE");
		if(size != null && StringUtils.isNotBlank(size.toArray(new String[size.size()]))){
			criteria.add(Restrictions.in("size.size", size));
		}
		
		// Query by colour
		List<String> colour = filters.get("COLOUR");
		if(colour != null && StringUtils.isNotBlank(colour.toArray(new String[colour.size()]))){
			criteria.add(Restrictions.in("colour.colour", colour));
		}
		
        criteria.setProjection(Projections.property("item.id"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        
		//Pagination
		//CurrentPage - 1 (in front end we class page 0 as page 1 so just -1
		currentPage -= 1;
		int start = currentPage * count;
		System.out.println("CurrentPage: " + currentPage + " Count: " + count + " Start: " + start);
        
		List<Integer> itemIDs = criteria.list();
		if(itemIDs.isEmpty()){
			System.out.println("Items Null");
			return null;
		}
		// The query above is equivalent to the following SQL:
		// SELECT item_id FROM item_spec WHERE size IN (SIZES_FROM_FILTERS) AND colour IN (COLOURS_FROM_FILTERS);
		
		criteria.setProjection(null);
		criteria.setProjection(Projections.property("colour.id"));
		List<Integer> colourIDs = criteria.list();
		if(colourIDs.isEmpty()){
			return null;
		}
		// The query above is equivalent to the following SQL:
		// SELECT colour_id FROM item_spec WHERE size IN (SIZES_FROM_FILTERS) AND colour IN (COLOURS_FROM_FILTERS); 
		
		
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
		
		List<ItemImage> itemImage = criteria2.list();
		if(itemImage.isEmpty()){
			return null;
		}
		// The query above is equivalent to the following SQL:
		// SELECT 
		// FROM item_image
		// JOIN item ON item_image.item_id = item.id
		// JOIN colour ON item_image.colour_id = colour.id
		// WHERE item_image.item_id IN (ITEM_IDS FROM CRITERIA 1) AND
		// item_image.colour_id IN (COLOUR_IDS FROM CRITERIA 1)
		// OFFSET N
		// LIMIT M;
		
		return itemImage;
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
		
		Map<String, List<FilterDTO>> result = new HashMap();
		DTOList<FilterDTO> dtoList = new DTOList();
		
		Set<String> keys = filters.keySet();
		for(String k : keys){
			if(!StringUtils.equals(k, "CATEGORY", "SIZE", "COLOUR")){
				return null;
			}
		}
		
		Criteria criteria = session.createCriteria(ItemSpec.class);
		List<String> category = filters.get("CATEGORY");
		
		criteria.createAlias("item", "item");
		criteria.createAlias("size", "size");
		criteria.createAlias("colour", "colour");
		
		criteria.add(Restrictions.eq("item.category", category.get(0)));
		
		List<String> sizes = filters.get("SIZE");
		if(sizes != null && !sizes.isEmpty()){
			criteria.add(Restrictions.in("size.size", sizes));
		}
		
		List<String> colours = filters.get("COLOUR");
		if(colours != null && !colours.isEmpty()){
			criteria.add(Restrictions.in("colour.colour", colours));
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
		return result;
	}
	
	
	
}
