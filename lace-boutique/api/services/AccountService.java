package api.services;

import java.sql.Timestamp;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.AccountDAO;
import api.sql.hibernate.entities.Account;
import api.utils.Responses;
import api.utils.SecureUtils;
import api.utils.StringUtils;
import api.utils.Utils;

public class AccountService {
	
	private HibernateQuery hibernateQuery = new HibernateQuery();
	
	/**
	 * Creates an Account for the user
	 * @param account
	 * @return string
	 */
	public String createAccount(Account account) {
		if(StringUtils.isBlank(account.getEmailAddress())){
	    	return Utils.getJsonBuilder().toJson(Responses.INVALID_EMAIL.getResponse());
	    }
	    
	    Account existingAccount = AccountDAO.getAccountByEmail(account.getEmailAddress());
	    if (existingAccount != null) {
	    	return Utils.getJsonBuilder().toJson(Responses.ACCOUNT_ALREADY_EXISTS.getResponse());
	    }
	    
	    if(StringUtils.isBlank(account.getPassword())){
	    	return Utils.getJsonBuilder().toJson(Responses.INVALID_PASSWORD.getResponse());
	    }
	    
	    if(!SecureUtils.validatePassword(account.getPassword())){
	    	return Utils.getJsonBuilder().toJson(Responses.INSECURE_PASSWORD.getResponse());
	    }

	    String hashedPassword = SecureUtils.bCrypt10Password(account.getPassword());
	    account.setPassword(hashedPassword);

		account.setCreatedOn(new Timestamp(System.currentTimeMillis()));
		account.setCreatedFrom("IP");
		account.setLastLogged(new Timestamp(System.currentTimeMillis()));
		account.setLoggedFrom("IP");
	    
	    if(saveAccount(account)){
	    	String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account);
	    	return Utils.getJsonBuilder().toJson(newToken);
	    } else {
	    	return Utils.getJsonBuilder().toJson(Responses.FAILURE_CREATING_ACCOUNT.getResponse());
	    }
	}
	
	/**
	 * Logs out - Returns a new token with no accout attached
	 * @return
	 */
	public String attemptLogout() {
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null);
    	return Utils.getJsonBuilder().toJson(newToken);
	}
	
	/**
	 * Attempts Login of Account - Returns the new Token (with Account)
	 * @param emailAddress
	 * @param unencryptedPassword
	 * @return token
	 */
	public String attemptLogin(String emailAddress, String unencryptedPassword) {
		if(StringUtils.isBlank(emailAddress, unencryptedPassword)){
	    	return Utils.getJsonBuilder().toJson(Responses.MISSING_EMAIL_OR_PASSWORD.getResponse());
	    }
	    
	    Account account = AccountDAO.getAccountByEmail(emailAddress);
	    if (account == null){
	    	return Utils.getJsonBuilder().toJson(Responses.INVAID_EMAIL_OR_PASSWORD.getResponse());
	    }
	    
	    account.setLastLogged(new Timestamp(System.currentTimeMillis()));
	    saveAccount(account);
	    
	    if (BCrypt.checkpw(unencryptedPassword, account.getPassword())){
	    	String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account);
	    	return Utils.getJsonBuilder().toJson(newToken);
	    } else {
	    	return Utils.getJsonBuilder().toJson(Responses.INVAID_EMAIL_OR_PASSWORD.getResponse());
	    }
	}
	
	/**
	 * Update Account
	 * @param account
	 * @return boolean
	 */
	public String updateAccount(Account account) {
		try {
			saveAccount(account);
		} catch (Exception e) {
			return Utils.getJsonBuilder().toJson(Responses.FAILED_TO_SAVE_ACCOUNT.getResponse());
		}
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account);
    	return Utils.getJsonBuilder().toJson(newToken);
	}
	
	
	/**
	 * Allows for saving of account throughout the Account Service
	 * @param account
	 * @return boolean
	 */
	private boolean saveAccount(Account account) {
		return hibernateQuery.saveOrUpdateObject(account);
	}
	
}
