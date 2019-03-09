package api.auth;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import api.entities.Token;


public class TokenInfo {
	
	private ObjectId userId;
    private DateTime issued;
    private DateTime expires;
    private Token token;
    
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

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}
    
}
