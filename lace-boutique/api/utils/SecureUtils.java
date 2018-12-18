package api.utils;

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
}
