package api.sql.hibernate;

public class ProjectionsExtension {
	public static SumProjection sumDistinct(String propertyName) {
		return new SumProjection( propertyName ).setDistinct();
	}
	
	public static CountProjectionCorrected countDistinct(String propertyName) {
		return new CountProjectionCorrected( propertyName ).setDistinct();
	}
}
