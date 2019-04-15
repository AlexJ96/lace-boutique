package api.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import api.mail.EmailService;
import api.sql.hibernate.HibernateQuery;
import api.sql.hibernate.dao.AccountDAO;
import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Address;
import api.sql.hibernate.entities.Cart;
import api.sql.hibernate.entities.Newsletter;
import api.sql.hibernate.entities.Order;
import api.sql.hibernate.entities.OrderItem;
import api.sql.hibernate.entities.PasswordReset;
import api.sql.hibernate.entities.Wishlist;
import api.sql.hibernate.entities.containers.OrdersContainer;
import api.utils.Responses;
import api.utils.SecureUtils;
import api.utils.StringUtils;
import api.utils.Utils;

public class AccountService {
	
	private HibernateQuery hibernateQuery = new HibernateQuery();
	private EmailService emailService = new EmailService();
	
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
			createWishlistForAccount(account);
			createCartForAccount(account);
			emailService.sendNewAccountRegistrationEmail(account);
	    	String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
	    	return Utils.getJsonBuilder().toJson(newToken);
	    } else {
	    	return Utils.getJsonBuilder().toJson(Responses.FAILURE_CREATING_ACCOUNT.getResponse());
	    }
	}
	
	/**
	 * Subscribes to newsletter (if has an account, updates preferences instead)
	 * @param emailAddress
	 * @return
	 */
	public boolean subscribeToNewsletter(String emailAddress) {
		Account account = AccountDAO.getAccountByEmail(emailAddress);
		
		if (account != null) {
			if (!account.isNewsletter()) {
				account.setNewsletter(true);
				saveAccount(account);
			}
		} else {
			Newsletter newsletter = new Newsletter();
			newsletter.setEmailAddress(emailAddress);
			hibernateQuery.saveOrUpdateObject(newsletter);
		}
		return true;
	}
	
	public boolean unsubscribeToNewsletter(String emailAddress) {
		Account account = AccountDAO.getAccountByEmail(emailAddress);
		
		if (account != null) {
			account.setNewsletter(false);
			saveAccount(account);
		} else {
			Newsletter newsletter = AccountDAO.getNewsletterByEmail(emailAddress);
			hibernateQuery.deleteObject(newsletter);
		}
		return true;
	}
	
	/**
	 * Generates a Reset Code and Sends an Email to Customer
	 * @param emailAddress
	 * @return
	 */
	public boolean beginPasswordReset(String emailAddress) {
		Account account = AccountDAO.getAccountByEmail(emailAddress.replace('"', ' ').trim());
		
		System.out.println(emailAddress);
		
		if (account == null)
			return false;
		
		PasswordReset passwordReset = generatePasswordReset(emailAddress.replace('"', ' ').trim());

		emailService.sendResetPasswordEmail(account, passwordReset);
		
		return true;
	}
	
	/**
	 * Confirms Password Reset and Changes to new password
	 * @param code
	 * @param newPassword
	 * @return
	 */
	public boolean confirmPasswordReset(String code, String newPassword) {
		PasswordReset passwordReset = checkPasswordReset(code.replace('"', ' ').trim());
		
		if (passwordReset == null)
			return false;
		
		if (!updateAccountPassword(passwordReset, newPassword.replace('"', ' ').trim()))
			return false;
		
		hibernateQuery.deleteObject(passwordReset);
		
		Account account = AccountDAO.getAccountByEmail(passwordReset.getEmail());
		
		passwordReset = generatePasswordReset(account.getEmailAddress());
		
		emailService.sendPasswordChangedEmail(account, passwordReset);
		
		return true;
	}
	
	/**
	 * Generate Password Reset 
	 * @param emailAddress
	 * @return PasswordReset
	 */
	private PasswordReset generatePasswordReset(String emailAddress) {
		Account account = AccountDAO.getAccountByEmail(emailAddress);
		
		if (account == null)
			return null;
		
		String[] resetCode = new String[3];
		resetCode[0] = RandomStringUtils.randomAlphanumeric(5, 10);
		resetCode[1] = RandomStringUtils.randomAlphanumeric(5, 10);
		resetCode[2] = RandomStringUtils.randomAlphanumeric(5, 10);
		
		String code = resetCode[0] + "-" + resetCode[1] + "-" + resetCode[2];
		
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setEmail(emailAddress);
		passwordReset.setCode(code);
		passwordReset.setExpiry(new Timestamp(System.currentTimeMillis()+20*60*1000));
		hibernateQuery.saveOrUpdateObject(passwordReset);
		
		return passwordReset;
	}
	
	/**
	 * Check Password Reset
	 * @param resetCode
	 * @return
	 */
	private PasswordReset checkPasswordReset(String resetCode) {
		PasswordReset passwordReset = AccountDAO.getPasswordReset(resetCode);
		if (passwordReset == null)
			return null;
		
		if (passwordReset.getExpiry().before(new Timestamp(System.currentTimeMillis()))) 
			return null;
		
		return passwordReset;
	}
	
	/**
	 * Update Account Password
	 * @param passwordReset
	 * @param password
	 * @return
	 */
	private boolean updateAccountPassword(PasswordReset passwordReset, String password) {
		Account account = AccountDAO.getAccountByEmail(passwordReset.getEmail());
		
		if (account == null)
			return false;
		
	    String hashedPassword = SecureUtils.bCrypt10Password(password);
	    account.setPassword(hashedPassword);
	    if (!saveAccount(account))
	    	return false;
	    
	    return true;
	}
	
	/**
	 * Gets Address For Account
	 * @param account
	 * @return
	 */
	public List<Address> getAddressesForAccount(Account account) {
		return AccountDAO.getAddressesForAccount(account);
	}
	
	/**
	 * Get's orders (container) order with items from order
	 * @param account
	 * @return
	 */
	public List<OrdersContainer> getOrders(Account account) {
		List<Order> ordersForAccount = AccountDAO.getOrdersByAccount(account);
		List<OrdersContainer> ordersContainerList = new ArrayList<OrdersContainer>();
		
		for (Order order : ordersForAccount) {
			List<OrderItem> itemsForOrder = AccountDAO.getOrderItemsByOrder(order);
			OrdersContainer ordersContainer = new OrdersContainer(order, itemsForOrder);
			ordersContainerList.add(ordersContainer);
		}
		
		return ordersContainerList;
	}
	
	
	/**
	 * Creates wishlist for account upon account creation
	 * @param account
	 */
	public void createWishlistForAccount(Account account) {
		Wishlist wishlist = new Wishlist();
		wishlist.setAccount(account);
		hibernateQuery.saveOrUpdateObject(wishlist);
	}
	
	/**
	 * Creates cart for account upon account creation
	 * @param account
	 */
	public void createCartForAccount(Account account) {
		Cart cart = new Cart();
		cart.setAccount(account);
		hibernateQuery.saveOrUpdateObject(cart);
	}
	
	/**
	 * Logs out - Returns a new token with no accout attached
	 * @return
	 */
	public String attemptLogout() {
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, null, null, null);
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
	    	String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
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
		String newToken = "Token: " + TokenService.generateToken(new ObjectId().toString(), 1L, account, null, null);
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
	
	/**
	 * Save or Update Address
	 * @param address
	 * @return
	 */
	public boolean saveOrUpdateAddress(Address address) {
		return hibernateQuery.saveOrUpdateObject(address);
	}
	
}
