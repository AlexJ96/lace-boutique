package api.sql.hibernate.dto;

public class FilterDTO extends DTO{
	String key;
	Long keyCount;
	
	public FilterDTO(){
		super();
	};
	
	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return this.key;
	}

	public void setKeyCount(Long keyCount){
		this.keyCount = keyCount;
	}

	public Long getKeyCount(){
		return this.keyCount;
	}

	@Override
	public String toString() {
		return "FilterDTO [key=" + key + ", keyCount=" + keyCount + "]";
	}
	
}
