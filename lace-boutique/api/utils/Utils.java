package api.utils;

import com.google.gson.*;

import api.annotation.GsonIgnore;

public class Utils {
	
//	private static Gson jsonBuilder = new GsonBuilder().create();
	private static Gson jsonBuilder = new GsonBuilder()
			.addSerializationExclusionStrategy(
			new ExclusionStrategy(){
        @Override
        public boolean shouldSkipField(FieldAttributes f)
        {
            return f.getAnnotation(GsonIgnore.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            return false;
        }
    })
    .create();
	
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
