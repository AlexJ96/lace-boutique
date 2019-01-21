package api.sql.hibernate.entities.containers;

import api.sql.hibernate.entities.ItemImage;
import api.sql.hibernate.entities.ItemSpec;

@Deprecated
public class ItemContainer {

	private ItemSpec itemSpec;
	private ItemImage itemImage;
	
	public ItemContainer(ItemSpec itemSpec, ItemImage itemImage) {
		this.itemSpec = itemSpec;
		this.itemImage = itemImage;
	}

	public ItemSpec getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(ItemSpec itemSpec) {
		this.itemSpec = itemSpec;
	}

	public ItemImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(ItemImage itemImage) {
		this.itemImage = itemImage;
	}

	@Override
	public String toString() {
		return "WishlistItemContainer [itemSpec=" + itemSpec + ", itemImage=" + itemImage + "]";
	}
	
}
