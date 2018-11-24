package api.endpoint;

import spark.Service;

public interface EndPoint {

	void configure(Service spark, String basePath);
	
}
