package api.sql.hibernate.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DTO<E extends DTO> {
	
	
	public void transformObject(Object[] object) throws Exception{
		Field[] fields = this.getClass().getDeclaredFields();
		int iCount = fields.length;
		if(iCount != object.length){
			throw new Exception("Object columns count is different from the DTO fields count.");
		}
		
		for(int i = 0; i < iCount; i++){
			Field f = fields[i];
			f.setAccessible(true);
			f.set(this, object[i]);
		}
		
	}
}
