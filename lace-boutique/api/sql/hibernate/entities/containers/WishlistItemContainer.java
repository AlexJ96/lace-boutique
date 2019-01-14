package api.sql.hibernate.entities.containers;

import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.Size;

public class WishlistItemContainer {

	private Size size;
	private ItemImage itemImage;
	
	public WishlistItemContainer(Size size, ItemImage itemImage) {
		this.size = size;
		this.itemImage = itemImage;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public ItemImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(ItemImage itemImage) {
		this.itemImage = itemImage;
	}

	@Override
	public String toString() {
		return "WishlistItemContainer [size=" + size + ", itemImage=" + itemImage + "]";
	}
	
}
