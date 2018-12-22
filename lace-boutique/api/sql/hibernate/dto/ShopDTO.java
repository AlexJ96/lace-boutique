package api.sql.hibernate.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import api.core.Api;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
import api.utils.StringUtils;

public class ShopDTO {
	
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
	 * 
	 * @param filters
	 * filters key only accept CATEGORY, SIZE and COLOUR.
	 * @return
	 */
	public static List<ItemImage> getItemImages(Map<String, String> filters){
		Set<String> keys = filters.keySet();
		for(String k : keys){
			if(!StringUtils.equals(k, "SIZE", "CATEGORY", "COLOUR")){
				return null;
			}
		}
		
		Criteria criteria = session.createCriteria(ItemImage.class);
		// Query by category.
		
		// @ALEXJ
		// According to your WEBSITE design, category must not be null, due to the navbar selection 
		// This ought to be checked for null before invoking this method.
		// Delete this comment after you've read it.
		String category = filters.get("CATEGORY");
		criteria.createAlias("itemImage.item", "item");
		criteria.add(Restrictions.eq("category", category));
		
		// Query by size
		String size = filters.get("SIZE");
		if(StringUtils.isNotBlank(size)){
			
		}
		
		// Query by colour
		
		return criteria.list();
	}
	
	
}
