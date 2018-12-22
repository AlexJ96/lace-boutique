package api.sql.hibernate.dto;

import java.util.ArrayList;
import java.util.List;

public class DTOList<E extends DTO> {
	List<E> dtoList;
	
	public DTOList(){
		dtoList = new ArrayList();
	}
	
	public List<E> getDTOList(Class dtoClass, List<Object[]> objectList) throws Exception{
		this.transformObjectList(dtoClass, objectList);
		return dtoList;
	}
	
	private void transformObjectList(Class dtoClass, List<Object[]> objectList) 
			throws Exception{
		List<E> result = new ArrayList();
		
		for(Object[] object : objectList){
			E e = (E)dtoClass.newInstance();
			e.transformObject(object);
			result.add(e);
		}
		
		this.dtoList = result;
	}
}
