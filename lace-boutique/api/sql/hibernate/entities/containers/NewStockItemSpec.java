package api.sql.hibernate.entities.containers;

import api.sql.hibernate.entities.Colour;
import api.sql.hibernate.entities.Size;

public class NewStockItemSpec {

	private Colour colour;
	
	private Size size;
	
	private int quantity;

	public Colour getColour()
	{
		return colour;
	}

	public void setColour(Colour colour)
	{
		this.colour = colour;
	}

	public Size getSize()
	{
		return size;
	}

	public void setSize(Size size)
	{
		this.size = size;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	@Override
	public String toString()
	{
		return "NewStockItemSpec [colour=" + colour + ", size=" + size + ", quantity=" + quantity + "]";
	}
	
}
