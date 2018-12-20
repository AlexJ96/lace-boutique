package api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utils for all Auth and Crypto
 * @author Jason
 *
 */
public class SecureUtils {
	public static String bCrypt10Password(String plainTextPassword){
		return hashPassword(plainTextPassword, 10);
	}
	
	public static String hashPassword(String plainTextPassword, int hash){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(hash));
	}
	
	public static boolean validatePasswordWithPattern(String plainTextPassword, String validPattern){
		Pattern pattern = Pattern.compile(validPattern);
		Matcher matcher = pattern.matcher(plainTextPassword);
		return matcher.matches();
	}
	
	public static boolean validatePassword(String plainTextPassword){
		return validatePasswordWithPattern(plainTextPassword, "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})");
	}
}
