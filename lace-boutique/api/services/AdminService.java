package api.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.ShopDAO;
import api.sql.hibernate.entities.Brand;
import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.Size;
import api.sql.hibernate.entities.containers.ItemDetailsContainer;
import api.sql.hibernate.entities.containers.ItemOptionsContainer;
import api.sql.hibernate.entities.containers.NewStockContainer;
import api.sql.hibernate.entities.containers.NewStockItemSpec;

public class AdminService {
	
	private HibernateQuery hibernateQuery = new HibernateQuery();
	

	public List<Item> getAllStock(int pageNumber) {
		return ShopDAO.getAllItems(pageNumber);
	}
	
	public ItemDetailsContainer getAllDetailsForItem(int itemId) {
		return ShopDAO.getAllDetailsForItem(itemId);
	}
	
	public List<ItemOptionsContainer> getDummyItemSpecContainer() {
		List<Colour> colours = ShopDAO.getAllColours();
		Map<String, ItemOptionsContainer> itemSpecMap = new HashMap<String, ItemOptionsContainer>();
		
		for (Colour colour : colours) {
			ItemOptionsContainer itemOptionContainer = new ItemOptionsContainer(colour.getColour(), new ArrayList<ItemSpec>());
			itemSpecMap.put(colour.getColour(), itemOptionContainer);	
		}
		
		List<ItemOptionsContainer> itemOptionsList = new ArrayList<ItemOptionsContainer>();
		for (ItemOptionsContainer itemOptionsContainer : itemSpecMap.values()) {
			itemOptionsList.add(itemOptionsContainer);
		}
		
		return itemOptionsList;
	}
	
	public List<Brand> getAllBrands() {
		return ShopDAO.getAllBrands();
	}
	
	public List<Size> getAllSizes() {
		return ShopDAO.getAllSizes();
	}
	
	public List<Colour> getAllColours() {
		return ShopDAO.getAllColours();
	}
	
	public Item createNewItem(Item item, String brandId) {
		Brand brand = ShopDAO.getBrandById(Integer.valueOf(brandId));
		if (brand != null) {
			item.setBrand(brand);
			return ShopDAO.saveItem(item);
		} else {
			return null;
		}
	}
	
	public boolean createNewItemData(Item item, NewStockContainer[] newStockContainers) {
		int index = 0;
		for (NewStockContainer newStockContainer : newStockContainers) {
			ItemImage itemImage = new ItemImage();
			itemImage.setItem(item);
			itemImage.setColour(newStockContainer.getColour());
			itemImage.setDefaultImage(index == 0 ? true : false);
			itemImage.setUrl(newStockContainer.getImageUrl());
			
			itemImage = ShopDAO.saveItemImage(itemImage);
			
			for (NewStockItemSpec newStockItemSpec: newStockContainer.getItemSpecs()) {
				ItemSpec itemSpec = new ItemSpec();
				itemSpec.setItem(item);
				itemSpec.setColour(newStockItemSpec.getColour());
				itemSpec.setSize(newStockItemSpec.getSize());
				itemSpec.setQuantity(newStockItemSpec.getQuantity());
				
				itemSpec = ShopDAO.saveItemSpec(itemSpec);
			}
			index++;
		}
		return true;
	}
	
	public boolean saveExistingItem(Item item, ItemOptionsContainer[] itemOptions) {
		boolean success = saveOrUpdateItem(item);
		if (!success)
			return false;
		
		for (ItemOptionsContainer itemOptionsContainer : itemOptions) {
			List<ItemSpec> itemSpecList = itemOptionsContainer.getItemSpecs();
			for (ItemSpec itemSpec : itemSpecList) {
				System.out.println(itemSpec.toString());
				ItemSpec updatedItemSpec = ShopDAO.saveItemSpec(itemSpec);
				if (updatedItemSpec == null)
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Save or Update Item
	 * @param address
	 * @return
	 */
	@Transactional
	public boolean saveOrUpdateItem(Item item) {
		return hibernateQuery.saveOrUpdateObject(item);
	}
	
	/**
	 * Save or Update ItemSpec
	 * @param address
	 * @return
	 */
	@Transactional
	public boolean saveOrUpdateItemSpec(ItemSpec itemSpec) {
		return hibernateQuery.saveOrUpdateObject(itemSpec);
	}
}
