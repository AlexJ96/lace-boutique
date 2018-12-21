package api.auth;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import api.sql.hibernate.entities.Account;


public class TokenInfo {
	
	private ObjectId userId;
    private DateTime issued;
    private DateTime expires;
    private Account account;
    
	public ObjectId getUserId() {
		return userId;
	}
	
	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	
	public DateTime getIssued() {
		return issued;
	}
	
	public void setIssued(DateTime issued) {
		this.issued = issued;
	}
	
	public DateTime getExpires() {
		return expires;
	}
	
	public void setExpires(DateTime expires) {
		this.expires = expires;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
    
}
