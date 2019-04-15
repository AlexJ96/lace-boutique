package api.endpoint.endpoints;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.endpoint.EndPoint;
import api.services.AdminService;
import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.containers.ItemOptionsContainer;
import api.sql.hibernate.entities.containers.NewStockContainer;
import api.utils.Utils;
import spark.Service;

public class AdminEndPoint implements EndPoint {
	
	private AdminService adminService = new AdminService();
	private Gson gson = new Gson();
	
	public AdminEndPoint() {
	}

	@Override
	public void configure(Service spark, String basePath) {
		spark.path(basePath + "/admin", () -> {
			
			spark.post("/list-items", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				return Utils.getJsonBuilder().toJson(adminService.getAllStock(object.get("PageNumber").getAsInt()));
			});
			
			spark.post("/load-details", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				return Utils.getJsonBuilder().toJson(adminService.getAllDetailsForItem(object.get("ItemId").getAsInt()));
			});
			
			spark.post("/load-brands", (request, response) -> {
				return Utils.getJsonBuilder().toJson(adminService.getAllBrands());
			});
			
			spark.post("/load-sizes", (request, response) -> {
				return Utils.getJsonBuilder().toJson(adminService.getAllSizes());
			});
			
			spark.post("/load-colours", (request, response) -> {
				return Utils.getJsonBuilder().toJson(adminService.getAllColours());
			});
			
			spark.post("/save-new-item", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				JsonObject itemObject = object.get("Item").getAsJsonObject();
				JsonArray itemSpecListJson = object.get("ItemSpecList").getAsJsonArray();
				
				String brandId = Utils.getJsonFieldAsString(itemObject, "brandId");
				Item item = gson.fromJson(object.get("Item"), Item.class);
				
				item = adminService.createNewItem(item, brandId);

				NewStockContainer[] newStockContainer = gson.fromJson(itemSpecListJson, NewStockContainer[].class);
				return Utils.getJsonBuilder().toJson(adminService.createNewItemData(item, newStockContainer));
			});
			
			spark.post("/load-dummy-item-details", (request, response) -> {
				return Utils.getJsonBuilder().toJson(adminService.getDummyItemSpecContainer());
			});
			
			spark.post("/save-existing-item", (request, response) -> {
				JsonObject object = new JsonParser().parse(request.body()).getAsJsonObject();
				System.out.println(request.body());
				JsonArray array = object.get("ItemSpecList").getAsJsonArray();
				
				System.out.println(array);
				
				Item item = gson.fromJson(object.get("Item"), Item.class);
				ItemOptionsContainer[] itemOptionsContainer = gson.fromJson(array, ItemOptionsContainer[].class);
				
				adminService.saveExistingItem(item, itemOptionsContainer);
				
				return Utils.getJsonBuilder().toJson(adminService.saveExistingItem(item, itemOptionsContainer));
			});
		});
	}
	
	

}
