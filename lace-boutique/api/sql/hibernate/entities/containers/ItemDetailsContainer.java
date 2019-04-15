package api.sql.hibernate.entities.containers;

import java.util.List;

import api.sql.hibernate.entities.Item;
import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;

public class ItemDetailsContainer {

	private Item item;
	
	private List<ItemImage> itemImage;
	
	private List<ItemSpec> itemSpec;

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	public List<ItemImage> getItemImage()
	{
		return itemImage;
	}

	public void setItemImage(List<ItemImage> itemImage)
	{
		this.itemImage = itemImage;
	}

	public List<ItemSpec> getItemSpec()
	{
		return itemSpec;
	}

	public void setItemSpec(List<ItemSpec> itemSpec)
	{
		this.itemSpec = itemSpec;
	}

	@Override
	public String toString()
	{
		return "ItemDetailsContainer [item=" + item + ", itemImage=" + itemImage + ", itemSpec=" + itemSpec + "]";
	}
	
}
