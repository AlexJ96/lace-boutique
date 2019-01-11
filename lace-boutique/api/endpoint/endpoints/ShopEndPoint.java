package api.endpoint.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.sql.hibernate.dao.ShopDAO;
import api.sql.hibernate.dto.FilterDTO;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.utils.StringUtils;
import api.utils.Utils;
import spark.Service;

public class ShopEndPoint implements EndPoint {
	
	public ShopEndPoint() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/shop", () -> {
			spark.get("/all-items", (request, response) -> {
				List<Item> shopItems = ShopDAO.getAllItems();
				System.out.println(shopItems.size());
				return Utils.getJsonBuilder().toJson(shopItems);
			});
			
			spark.post("/all-items-brand", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String brand = Utils.getJsonFieldAsString(jobject, "Brand");
			    System.out.println(brand);
			    
			    
				List<Item> shopItems = ShopDAO.getAllItemsByBrand(brand);
				System.out.println(shopItems.size());
				return Utils.getJsonBuilder().toJson(shopItems);
			});
			
			spark.post("/getFilters", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();

			    Map<String, List<String>> filters = new HashMap();
				
				List<String> categoryList = new ArrayList();
			    String category = Utils.getJsonFieldAsString(jobject, "Category");
			    
			    if (StringUtils.isNotBlank(category)) {
			    	categoryList.add(category);
			    }
			    
			    System.out.println(Utils.getJsonFieldAsString(jobject, "Size"));

				List<String> sizesList = new ArrayList();
			    String sizes = Utils.getJsonFieldAsString(jobject, "Size");
			    if (StringUtils.isNotBlank(sizes)) {
			    	String[] sizeArray = sizes.split(",");
			    	for (String size : sizeArray) {
			    		sizesList.add(size);
			    	}
			    }
			    
			    List<String> coloursList = new ArrayList();
			    String colours = Utils.getJsonFieldAsString(jobject, "Colour");
			    if (StringUtils.isNotBlank(colours)) {
			    	String[] coloursArray = colours.split(",");
			    	for (String colour : coloursArray) {
			    		coloursList.add(colour);
			    	}
			    }
			    
			    if (!categoryList.isEmpty()) {
			    	filters.put("CATEGORY", categoryList);
			    }
			    
			    if (!sizesList.isEmpty()) {
					filters.put("SIZE", sizesList);
			    }

			    if (!coloursList.isEmpty()) {
					filters.put("COLOUR", coloursList);
			    }

			    if (!filters.isEmpty()) {
					Map<String,List<FilterDTO>> result = ShopDAO.getFilters(filters);
			    	return Utils.getJsonBuilder().toJson(result);
			    } else {
			    	return "";
			    }
			});
			
			spark.post("/getItems", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String pageCount = Utils.getJsonFieldAsString(jobject, "CurrentPage");
			    String count = Utils.getJsonFieldAsString(jobject, "Count");
			    //System.out.println("Page Count: " + pageCount + " count: " + count);
			    
			    int currentPage = 0;
			    int itemCount = 0;
			    
			    if (pageCount != null || StringUtils.isNotBlank(pageCount)) {
			    	currentPage = Integer.valueOf(pageCount);
			    }
			    
			    if (count != null || StringUtils.isNotBlank(count)) {
			    	itemCount = Integer.valueOf(count);
			    }
			    
			    //System.out.println(currentPage + " " + itemCount);

				Map<String, List<String>> filters = new HashMap();
				
				List<String> categoryList = new ArrayList();
			    String category = Utils.getJsonFieldAsString(jobject, "Category");
			    
			    if (StringUtils.isNotBlank(category)) {
			    	categoryList.add(category);
			    }
			    
			    System.out.println(Utils.getJsonFieldAsString(jobject, "Size"));

				List<String> sizesList = new ArrayList();
			    String sizes = Utils.getJsonFieldAsString(jobject, "Size");
			    if (StringUtils.isNotBlank(sizes)) {
			    	String[] sizeArray = sizes.split(",");
			    	for (String size : sizeArray) {
			    		sizesList.add(size);
			    	}
			    }
			    
			    List<String> coloursList = new ArrayList();
			    String colours = Utils.getJsonFieldAsString(jobject, "Colour");
			    if (StringUtils.isNotBlank(colours)) {
			    	String[] coloursArray = colours.split(",");
			    	for (String colour : coloursArray) {
			    		coloursList.add(colour);
			    	}
			    }
			    
			    if (!categoryList.isEmpty()) {
			    	filters.put("CATEGORY", categoryList);
			    }
			    
			    if (!sizesList.isEmpty()) {
					filters.put("SIZE", sizesList);
			    }

			    if (!coloursList.isEmpty()) {
					filters.put("COLOUR", coloursList);
			    }
			    
			    if (!filters.isEmpty()) {
			    	// CHANGED HERE!!!!!!
			    	itemCount = 10;
			    	List<ItemImage> images = ShopDAO.getItemImage(filters, 1, itemCount);
			    	if (images != null) {
			    		System.out.println(images.size());
			    		System.out.println(images);
			    	}
			    	List<ItemImage> images2 = ShopDAO.getItemImage(filters, 2, itemCount);
			    	if (images2 != null) {
			    		System.out.println(images2.size());
			    		System.out.println(images2);
			    	}
			    	return "";
			    	//return Utils.getJsonBuilder().toJson(images);
			    } else {
			    	return "";
			    }
			});
			
		});
	}
	

}
