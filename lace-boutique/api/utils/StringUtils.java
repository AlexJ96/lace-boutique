package api.utils;

import org.eclipse.jetty.util.StringUtil;

public class StringUtils {
	public static boolean isBlank(String string){
		return StringUtil.isBlank(string);
	}
	
	public static boolean isNotBlank(String string){
		return StringUtil.isNotBlank(string);
	}
	
	public static boolean isBlank(String ...strings){
		for(String s : strings){
			if(StringUtil.isNotBlank(s)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNotBlank(String ...strings){
		for(String s : strings){
			if(StringUtil.isBlank(s)){
				return false;
			}
		}
		return true;
	}
	
	
}
