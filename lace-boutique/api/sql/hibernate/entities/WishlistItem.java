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
	@JoinColumn(name = "item_id")
	private Item item;

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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "WishlistItem [id=" + id + ", wishlist=" + wishlist + ", item=" + item + "]";
	}
	
}
