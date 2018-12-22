package api.utils;

import java.util.Collection;

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
	

    
    public static String changeCharInPosition(int position, String ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch.charAt(0);
        return new String(charArray);
    }
	
    /**
     * Logic is OR. If the string equals to any strings, returns true immediately.
     * @param string
     * @param strings
     * @return
     */
	public static boolean equals(String string, String...strings ){
		if(isBlank(string)){
			return false;
		}
		for(String s : strings){
			if(string.equals(s)){
				return true;
			}
		}
		return false;
	}
}
