package api.sql.hibernate.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.transaction.Transactional;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import api.sql.hibernate.dao.EntityDAO;

@Entity
@Table(name = "item_spec")
public class ItemSpec implements Serializable{
	
	private static final long serialVersionUID = 1L;

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
	
	public List<ItemImage> getItemImages(){
		return EntityDAO.getImageOf(this, false);
	}

	public List<ItemImage> getDefaultItemImages(){
		return EntityDAO.getImageOf(this, true);
	}
	
	@Override
	public String toString() {
		return "ItemSpec [id=" + id + ", item=" + item + ", size=" + size + ", quantity=" + quantity + ", colour="
				+ colour + "]";
	}
	
}