package api.endpoint.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.sql.hibernate.dto.ShopDTO;
import api.sql.hibernate.entities.Item;
import api.utils.StringUtils;
import api.utils.Utils;
import spark.Service;

public class ShopEndPoint implements EndPoint {
	
	public ShopEndPoint() {}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/shop", () -> {
			spark.get("/all-items", (request, response) -> {
				List<Item> shopItems = ShopDTO.getAllItems();
				System.out.println(shopItems.size());
				return Utils.getJsonBuilder().toJson(shopItems);
			});
			
			spark.post("/all-items-brand", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    String brand = Utils.getJsonFieldAsString(jobject, "Brand");
			    System.out.println(brand);
			    
			    
				List<Item> shopItems = ShopDTO.getAllItemsByBrand(brand);
				System.out.println(shopItems.size());
				return Utils.getJsonBuilder().toJson(shopItems);
			});
			
			spark.post("/items-by-filter", (request, response) -> {
				JsonElement jelement = new JsonParser().parse(request.body());
			    JsonObject  jobject = jelement.getAsJsonObject();
			    
			    /**
			     * Standard Filter should contain:
			     * Brand
			     * Colour
			     * Size
			     * Category
			     */
			    Map<String, String> filter = new HashMap<String, String>();
			    
			    String brand = Utils.getJsonFieldAsString(jobject, "Brand");
			    if (StringUtils.isNotBlank(brand)) {
			    	filter.put("Brand", brand);
			    }
			    
			    String colour = Utils.getJsonFieldAsString(jobject, "Colour");
			    if (StringUtils.isNotBlank(colour)) {
			    	filter.put("Colour", colour);
			    }
			    
			    String size = Utils.getJsonFieldAsString(jobject, "Size");
			    if (StringUtils.isNotBlank(size)) {
			    	filter.put("Size", size);
			    }
			    
			    String category = Utils.getJsonFieldAsString(jobject, "Category");
			    if (StringUtils.isNotBlank(category)) {
			    	filter.put("Category", category);
			    }

			    List<Item> shopItems = ShopDTO.filterItems(filter);
				return Utils.getJsonBuilder().toJson(shopItems);
			});
			
		});
	}
	

}
