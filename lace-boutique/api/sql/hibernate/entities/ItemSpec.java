package api.sql.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "item_spec")
public class ItemSpec {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	// Many ItemSpec to one Item.  
	// An item can have more than one size/colour combinations.
	@ManyToOne	
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "size_id")
	private Size size;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "colour_id")
	private Colour colour;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Colour getColour(){
		return this.colour;
	}
	
	public void setColour(Colour colour){
		this.colour = colour;
	}

	@Override
	public String toString() {
		return "ItemSpec [id=" + id + ", item=" + item + ", size=" + size + ", quantity=" + quantity + "]";
	}
	
}