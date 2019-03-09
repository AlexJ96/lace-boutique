package api.services;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import api.entities.Token;
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

public class TokenService {
	
	private static final String AUDIENCE = "LaceBoutiqueCustomers";
    private static final String ISSUER = "LaceBoutiqueAPI";
    private static final String SIGNING_KEY = ";Sd`o#xO8t('/<8R=8e+^#0RY.%=vGgXV|B+V$wOj*STlY47RaMayolpaj&t$Z*1J_3__a:A5*d/E=?Uf0Y=>[8]YYn>uhr1BNA:R}eyL57ITn_22%ZFB^*";
    
    private static Calendar cal = Calendar.getInstance();
    private static Gson gson = new Gson();
    private static WishlistService wishlistService = new WishlistService();
    

	public static String generateToken(String clientId, Long duration, Account account) {
		HmacSHA256Signer signer;
		try {
			signer = new HmacSHA256Signer(ISSUER, null, SIGNING_KEY.getBytes());
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
			
		JsonToken jsonToken = new net.oauth.jsontoken.JsonToken(signer);
		jsonToken.setAudience(AUDIENCE);
		jsonToken.setIssuedAt(new org.joda.time.Instant(cal.getTimeInMillis()));
		jsonToken.setExpiration(new org.joda.time.Instant(cal.getTimeInMillis() + 1000L * 60L * 60L * 24L * duration));
        
        JsonObject request = new JsonObject();
        request.addProperty("clientId", clientId);
        
        Token token = new Token();
        
        if (account == null) {
        	account = new Account();
        }
    	token.setAccount(account);
    	token.setWishlist(wishlistService.getWishlistForAccount(account));
    	
    	JsonElement jsonElement = Utils.getJsonBuilder().toJsonTree(token);
    	request.add("token", jsonElement);
		
    	JsonObject payload = jsonToken.getPayloadAsJsonObject();
        payload.add("info", request);

        try {
            return jsonToken.serializeAndSign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static Token decodeToken(String token) {
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
                public void check(JsonObject payload) throws SignatureException {}
            };
            
            JsonTokenParser parser = new JsonTokenParser(locators, checker);
            JsonToken jsonToken;
            try {
            	jsonToken = parser.verifyAndDeserialize(token);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
            JsonObject payload = jsonToken.getPayloadAsJsonObject();
            
            JsonObject infoJson = payload.getAsJsonObject("info");
            JsonObject tokenJson = infoJson.getAsJsonObject("token");
            
            Token tokenObject = gson.fromJson(tokenJson, Token.class);
            
            return tokenObject;
		} catch (InvalidKeyException e1) {
	        throw new RuntimeException(e1);
	    }
	}
	
	public static boolean verifyToken(String token) {
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
                public void check(JsonObject payload) throws SignatureException {}

            };
            JsonTokenParser parser = new JsonTokenParser(locators, checker);
            JsonToken jt;
            try {
                jt = parser.verifyAndDeserialize(token);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
            
            JsonObject payload = jt.getPayloadAsJsonObject();
            
            String issuer = Utils.getJsonFieldAsString(payload, "iss");
            
            JsonObject info = payload.getAsJsonObject("info");
            String userIdString =  Utils.getJsonFieldAsString(info, "clientId");
        
            if (issuer.equals(ISSUER) && !StringUtils.isBlank(userIdString)) {
                return true;
            } else {
                return false;
            }
        } catch (InvalidKeyException e1) {
            throw new RuntimeException(e1);
        }
    }
	
	public static int validateToken(String token) {
    	if (token == null || token == "")
    		return 200; //Needed to Continue to the second request which carries the token, sending 401 halts the second call
    	
		boolean validToken = false;
		try {
			validToken = verifyToken(token);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 401;
		}
		
		if (!validToken) {
			System.out.println("Token Info = Null");
			return 401;
		}
		
		return 0;
    }
	
	public static String refreshToken(String token) {
		if (verifyToken(token)) {
			Token tokenObject = decodeToken(token);
			if (tokenObject.getAccount() != null) {
				return generateToken(new ObjectId().toString(), 1L, tokenObject.getAccount());
			} else {
				return generateToken(new ObjectId().toString(), 1L, null);
			}
		} else {
			return generateToken(new ObjectId().toString(), 1L, null);
		}
    }
}
