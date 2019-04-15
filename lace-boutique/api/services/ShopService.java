package api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.sql.hibernate.dao.ShopDAO;
import api.sql.hibernate.entities.Discount;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;
import api.sql.hibernate.entities.containers.ItemOptionsContainer;

public class ShopService {
	
	public List<ItemOptionsContainer> getSpecsForItem(int itemId) {
		List<ItemSpec> itemSpecList = ShopDAO.getSpecsForItem(itemId);
		Map<String, ItemOptionsContainer> itemSpecMap = new HashMap<String, ItemOptionsContainer>();
		
		for (ItemSpec itemSpec : itemSpecList) {
			
			for(ItemImage ii : itemSpec.getItemImages()){
				System.out.println(ii.getId() + ", " +itemSpec.getColour().getId() + ", " + itemSpec.getItem().getId() + ", " + ii.getUrl());
			}
			if (itemSpecMap.containsKey(itemSpec.getColour().getColour())) {
				ItemOptionsContainer itemOptionsContainer = itemSpecMap.get(itemSpec.getColour().getColour());
				itemOptionsContainer.getItemSpecs().add((itemSpec));
				itemSpecMap.replace(itemSpec.getColour().getColour(), itemOptionsContainer);
			} else {
				ItemOptionsContainer itemOptionContainer = new ItemOptionsContainer(itemSpec.getColour().getColour(), new ArrayList<ItemSpec>());
				itemSpecMap.put(itemSpec.getColour().getColour(), itemOptionContainer);
			}
		}
		
		List<ItemOptionsContainer> itemOptionsList = new ArrayList<ItemOptionsContainer>();
		for (ItemOptionsContainer itemOptionsContainer : itemSpecMap.values()) {
			itemOptionsList.add(itemOptionsContainer);
		}
		
		return itemOptionsList;
	}
	
	public double applyDiscount(String discountCode, double totalPrice) {
		Discount discount = ShopDAO.checkDiscountCode(discountCode.replace('"', ' ').trim());
		System.out.println(discount.toString());
		if (discount != null) {
			Date date = new Date();
			if (date.before(discount.getValidFrom()) || date.after(discount.getValidTo()) ) {
				return 0.00;
			}
			if (discount.getMinSpend() > totalPrice) {
				return 0.00;
			}
			if (discount.isFreeDelivery()) {
				return 9999.99;
			} else {
				return discount.getAmountOff();
			}
		}
		return 0.0;
	}
	
}
