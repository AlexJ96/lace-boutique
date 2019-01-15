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
@Table(name = "cart_item")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@OneToOne
	@JoinColumn(name = "item_spec_id")
	private ItemSpec itemSpec;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public ItemSpec getItemSpec() {
		return itemSpec;
	}

	public void setItemSpec(ItemSpec itemSpec) {
		this.itemSpec = itemSpec;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", cart=" + cart + ", itemSpec="
				+ itemSpec + "]";
	}
	
}
