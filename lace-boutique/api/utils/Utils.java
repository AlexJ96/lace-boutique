package api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	
	
	
}
