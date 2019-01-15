package api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.auth.Authenticator;
import api.endpoint.EndPoint;
import api.utils.Utils;
import spark.Service;

public class RestContext {
	
	private static final Logger logger = LoggerFactory.getLogger(RestContext.class);

    private final Service spark;

    private final String basePath;
    
    private final String url = "http://localhost:8080";
    
    private final String[] unauthorizedEndPoints = { "/token/request-token", "/token/refresh-token", "/shop/getFilters", "/shop/getItems" };

    public RestContext(int port, String basePath) {
        this.basePath = basePath;
        spark = Service.ignite().port(port);
    }

    public void addEndpoint(EndPoint endpoint) {
        endpoint.configure(spark, basePath);
        logger.info("REST endpoints registered for {}.", endpoint.getClass().getSimpleName());
    }
    
    public void enableCors() {
    	spark.options("/*", (request,response)->{
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if(accessControlRequestMethod != null){
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    	
    	spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "Content-Type, LBT");
            
            boolean needsAuth = true;
            
            for (String unauthorizedEndPoint : unauthorizedEndPoints) {
            	if (request.url().equalsIgnoreCase(url + basePath + unauthorizedEndPoint))
            		needsAuth = false;
            }
            
            if (needsAuth == true) {
	            String token = request.headers("LBT");
				int responseId = Authenticator.validateAuthToken(token);
				
				if (responseId != 0) {
					spark.halt(responseId, Utils.getJsonBuilder().toJson("Unauthorised Access"));
				}
            }
            System.out.println(request.url());
    	});
        logger.info("CORS support enabled.");
    }

}
