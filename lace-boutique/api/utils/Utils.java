package api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Utils {
	
	private static Gson jsonBuilder = new GsonBuilder().create();
	
	public Utils() {
	}

	/**
	 * @return the gsonBuilder
	 */
	public static Gson getJsonBuilder() {
		return jsonBuilder;
	}
	
	/**
	 * Extract data from JsonObject as String and check if the given field is 
	 * null at the same time.
	 * @param fieldName
	 * @return
	 */
	public static String getJsonFieldAsString(JsonObject jobject, String fieldName){
		JsonElement jElement = jobject.get(fieldName);
		if(jElement == null){
			return null;
		}
		return jobject.get(fieldName).getAsString();
	}
	
	public static JsonArray getJsonFieldAsArray(JsonObject jobject, String fieldName){
		JsonElement jElement = jobject.get(fieldName);
		if(jElement == null){
			return null;
		}
		return jobject.get(fieldName).getAsJsonArray();
	}
	
}
