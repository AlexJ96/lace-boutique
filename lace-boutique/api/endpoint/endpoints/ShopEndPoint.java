package api.endpoint.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.services.ShopService;
import api.sql.hibernate.dao.ShopDAO;
import api.sql.hibernate.dto.FilterDTO;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.utils.StringUtils;
import api.utils.Utils;
import spark.Service;

public class ShopEndPoint implements EndPoint {
	
	private ShopService shopService = new ShopService();
	
	public ShopEndPoint() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/shop", () -> {
			spark.get("/all-items", (request, response) -> {
//				List<Item> shopItems = ShopDAO.getAllItems();
//				System.out.println(shopItems.size());
//				return Utils.getJsonBuilder().toJson(shopItems);
				return "";
			});
			
			spark.post("/item-spec", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				return Utils.getJsonBuilder().toJson(shopService.getSpecsForItem(object.get("ItemId").getAsInt()));
			});
			
			spark.post("/item-image-id", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				return Utils.getJsonBuilder().toJson(shopService.getItemImageById(object.get("ItemImageId").getAsInt()));
			});
			
			spark.post("/apply-discount", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				return Utils.getJsonBuilder().toJson(shopService.applyDiscount(object.get("DiscountCode").toString(), object.get("TotalPrice").getAsDouble()));
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
			    
			    List<String> brandsList = new ArrayList();
			    String brands = Utils.getJsonFieldAsString(jobject, "Brand");
			    if (StringUtils.isNotBlank(brands)) {
			    	String[] brandsArray = brands.split(",");
			    	for (String brand : brandsArray) {
			    		brandsList.add(brand);
			    	}
			    }
			    
			    List<String> typeList = new ArrayList();
			    String types = Utils.getJsonFieldAsString(jobject, "Type");
			    if (StringUtils.isNotBlank(types)) {
			    	String[] typeArray = types.split(",");
			    	for (String type : typeArray) {
			    		typeList.add(type);
			    	}
			    }
			    
			    if (!categoryList.isEmpty()) {
			    	filters.put("CATEGORY", categoryList);
			    }
			    
			    if (!typeList.isEmpty()) {
			    	filters.put("TYPES", typeList);
			    }
			    
			    if (!sizesList.isEmpty()) {
					filters.put("SIZE", sizesList);
			    }

			    if (!coloursList.isEmpty()) {
					filters.put("COLOUR", coloursList);
			    }
			    
			    if (!brandsList.isEmpty()) {
			    	filters.put("BRAND", brandsList);
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
			    
			    String pageCount = StringUtils.isBlank(Utils.getJsonFieldAsString(jobject, "CurrentPage")) ? "1" : Utils.getJsonFieldAsString(jobject, "CurrentPage");
			    String count = StringUtils.isBlank(Utils.getJsonFieldAsString(jobject, "Count")) ? "25" : Utils.getJsonFieldAsString(jobject, "Count");
			    String order = StringUtils.isBlank(Utils.getJsonFieldAsString(jobject, "Order")) ? "" : Utils.getJsonFieldAsString(jobject, "Order");
			    
			    int currentPage = 1;
			    int itemCount = 25;
			    
			    if (pageCount != null || StringUtils.isNotBlank(pageCount)) {
			    	currentPage = Integer.valueOf(pageCount);
			    }
			    
			    if (count != null || StringUtils.isNotBlank(count)) {
			    	itemCount = Integer.valueOf(count);
			    }

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
			    
			    List<String> brandsList = new ArrayList();
			    String brands = Utils.getJsonFieldAsString(jobject, "Brand");
			    if (StringUtils.isNotBlank(brands)) {
			    	String[] brandsArray = brands.split(",");
			    	for (String brand : brandsArray) {
			    		brandsList.add(brand);
			    	}
			    }
			    
			    List<String> typesList = new ArrayList();
			    String types = Utils.getJsonFieldAsString(jobject, "Type");
			    if (StringUtils.isNotBlank(types)) {
			    	String[] typesArray = types.split(",");
			    	for (String type : typesArray) {
			    		typesList.add(type);
			    	}
			    }
			    
			    if (!categoryList.isEmpty()) {
			    	filters.put("CATEGORY", categoryList);
			    }
			    
			    if (!typesList.isEmpty()) {
			    	filters.put("TYPES", typesList);
			    }
			    
			    if (!sizesList.isEmpty()) {
					filters.put("SIZE", sizesList);
			    }

			    if (!coloursList.isEmpty()) {
					filters.put("COLOUR", coloursList);
			    }
			    
			    if (!brandsList.isEmpty()) {
					filters.put("BRAND", brandsList);
			    }
			    
			    if (!filters.isEmpty()) {
			    	List<ItemImage> images = ShopDAO.getItemImage(filters, currentPage, itemCount, order);
			    	if (images != null) {
				    	return Utils.getJsonBuilder().toJson(images);
			    	} else {
			    		return "";
			    	}
			    } else {
			    	return "";
			    }
			});
			
		});
	}
	

}
