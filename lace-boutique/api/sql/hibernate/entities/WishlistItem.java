package api.sql.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wishlist_item")
public class WishlistItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "wishlist_id")
	private Wishlist wishlist;

	@OneToOne
	@JoinColumn(name = "item_spec_id")
	private ItemSpec itemSpec;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}

	public ItemSpec getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(ItemSpec itemSpec) {
		this.itemSpec = itemSpec;
	}

	@Override
	public String toString() {
		return "WishlistItem [id=" + id + ", wishlist=" + wishlist + ", itemSpec="
				+ itemSpec + "]";
	}
	
}
