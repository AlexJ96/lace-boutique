package api.sql.hibernate.entities;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import api.annotation.GsonIgnore;

@Entity
@Table(name = "cart_item")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@GsonIgnore
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@OneToOne
	private ItemSpec itemSpec;
	
	@Column(name = "quantity")
	private int quantity;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", cart=" + cart + ", itemSpec=" + itemSpec + ", quantity=" + quantity + "]";
	}
	
}
