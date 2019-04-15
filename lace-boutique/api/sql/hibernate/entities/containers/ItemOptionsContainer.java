package api.sql.hibernate.entities.containers;

import java.util.List;

import api.sql.hibernate.entities.ItemSpec;

public class ItemOptionsContainer {

	private String color;
	
	private List<ItemSpec> itemSpecs;
	
	public ItemOptionsContainer(String color, List<ItemSpec> itemSpecs) {
		this.color = color;
		this.itemSpecs = itemSpecs;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<ItemSpec> getItemSpecs() {
		return itemSpecs;
	}

	public void setItemSpecs(List<ItemSpec> itemSpecs) {
		this.itemSpecs = itemSpecs;
	}
	
	@Override
	public String toString(){
		return "ItemOptionsContainer [colour="+ color + ", itemSpecs=" + itemSpecs + "]";
	}
	
}
