package api.sql.hibernate.entities.containers;

import java.util.List;

import api.sql.hibernate.entities.Colour;

public class NewStockContainer {
	
	private Colour colour;
	
	private String imageUrl;
	
	private List<NewStockItemSpec> itemSpecs;

	public Colour getColour()
	{
		return colour;
	}

	public void setColour(Colour colour)
	{
		this.colour = colour;
	}

	public List<NewStockItemSpec> getItemSpecs()
	{
		return itemSpecs;
	}

	public void setItemSpecs(List<NewStockItemSpec> itemSpecs)
	{
		this.itemSpecs = itemSpecs;
	}
	

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString()
	{
		return "NewStockContainer [colour=" + colour + ", imageUrl=" + imageUrl + ", itemSpecs=" + itemSpecs + "]";
	}

}
