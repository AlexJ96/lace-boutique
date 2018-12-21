package api.auth;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import api.core.Api;
import api.sql.hibernate.entities.Account;
import api.utils.StringUtils;
import api.utils.Utils;
import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;

public class Authenticator {

    private static final String AUDIENCE = "LaceBoutiqueCustomers";

    private static final String ISSUER = "LaceBoutiqueAPI";

    private static final String SIGNING_KEY = ";Sd`o#xO8t('/<8R=8e+^#0RY.%=vGgXV|B+V$wOj*STlY47RaMayolpaj&t$Z*1J_3__a:A5*d/E=?Uf0Y=>[8]YYn>uhr1BNA:R}eyL57ITn_22%ZFB^*";
    
    
    public static String generateWebToken(String clientId, Long duration) {
    	return generateWebToken(clientId, duration, null);
    }
    
    public static String generateWebToken(String clientId, Long duration, Account account) {
    	/*
    	 * Get Current Calender Instance
    	 */
    	Calendar cal = Calendar.getInstance();
    	
    	/*
    	 * Generate Signing Algorithm 
    	 */
        HmacSHA256Signer signer;
        try {
            signer = new HmacSHA256Signer(ISSUER, null, SIGNING_KEY.getBytes());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        
        /*
         * Create JSON Token
         */
        JsonToken token = new net.oauth.jsontoken.JsonToken(signer);
        token.setAudience(AUDIENCE);
        token.setIssuedAt(new org.joda.time.Instant(cal.getTimeInMillis()));
        token.setExpiration(new org.joda.time.Instant(cal.getTimeInMillis() + 1000L * 60L * 60L * 24L * duration));
        //token.setExpiration(new org.joda.time.Instant(cal.getTimeInMillis() + 1000L));
        
        JsonObject request = new JsonObject();
        request.addProperty("clientId", clientId);
        request.addProperty("account", account != null ? Utils.getJsonBuilder().toJson(account) : "");

        JsonObject payload = token.getPayloadAsJsonObject();
        payload.add("info", request);

        try {
            return token.serializeAndSign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String refreshToken(String accountBody) {
		JsonElement accountElement = new JsonParser().parse(accountBody);
	    JsonObject  accountObject = accountElement.getAsJsonObject();
	    
	    String accountJson = Utils.getJsonFieldAsString(accountObject, "Account");
	    Account account = new Gson().fromJson(accountJson, Account.class);
	    
    	if (account != null) {
    		account = (Account) Api.getHibernateQuery().getObject(Account.class, account.getId());
    		return generateWebToken(new ObjectId().toString(), 1L, account);
    	} else {
    		return generateWebToken(new ObjectId().toString(), 1L);
    	}
    }

    public static TokenInfo verifyToken(String token)  
    {
        try {
            final Verifier hmacVerifier = new HmacSHA256Verifier(SIGNING_KEY.getBytes());

            VerifierProvider hmacLocator = new VerifierProvider() {

                @Override
                public List<Verifier> findVerifier(String id, String key){
                    return Lists.newArrayList(hmacVerifier);
                }
            };
            VerifierProviders locators = new VerifierProviders();
            locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);
            net.oauth.jsontoken.Checker checker = new net.oauth.jsontoken.Checker(){

                @Override
                public void check(JsonObject payload) throws SignatureException {
                    // don't throw - allow anything
                }

            };
            JsonTokenParser parser = new JsonTokenParser(locators,
                    checker);
            JsonToken jt;
            try {
                jt = parser.verifyAndDeserialize(token);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
            JsonObject payload = jt.getPayloadAsJsonObject();
            TokenInfo t = new TokenInfo();
            String issuer = payload.getAsJsonPrimitive("iss").getAsString();
            String userIdString =  payload.getAsJsonObject("info").getAsJsonPrimitive("clientId").getAsString();
            
            //Storing Account in the Token
            String accountJson = payload.getAsJsonObject("info").getAsJsonPrimitive("account").getAsString();
            if (StringUtils.isNotBlank(accountJson)) {
                Account account = new Gson().fromJson(accountJson, Account.class);
                t.setAccount(account);
            }
            
        
            if (issuer.equals(ISSUER) && !StringUtils.isBlank(userIdString))
            {
                t.setUserId(new ObjectId(userIdString));
                t.setIssued(new DateTime(payload.getAsJsonPrimitive("iat").getAsLong()));
                t.setExpires(new DateTime(payload.getAsJsonPrimitive("exp").getAsLong()));
                return t;
            }
            else
            {
                return null;
            }
        } catch (InvalidKeyException e1) {
            throw new RuntimeException(e1);
        }
    }
    
    public static int validateAuthToken(String token) {
    	if (token == null || token == "")
    		return 200; //Needed to Continue to the second request which carries the token, sending 401 halts the second call
    	
		token = token.substring(14);
		TokenInfo tokenInfo = null;
		
		try {
			tokenInfo = verifyToken(token);
		} catch (Exception e) {
			return 401;
		}
		
		if (tokenInfo == null)
			return 401;
		
		return 0;
    	
    }

    
}
