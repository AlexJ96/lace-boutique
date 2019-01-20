package api.sql.hibernate.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Where;

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
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumns({
        @JoinColumn(name = "item_id", referencedColumnName = "id"),
        @JoinColumn(name = "colour_id", referencedColumnName = "colour_id")
	})
	private List<ItemImage> itemImages;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumns({
        @JoinColumn(name = "item_id", referencedColumnName = "id"),
        @JoinColumn(name = "colour_id", referencedColumnName = "colour_id")
	})
	@Where(clause="default_image = '1'")
	private List<ItemImage> defaultImage;
	
	
	
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
		return this.itemImages;
	}

	public List<ItemImage> getDefaultItemImages(){
		return this.defaultImage;
	}
	
	@Override
	public String toString() {
		return "ItemSpec [id=" + id + ", item=" + item + ", size=" + size + ", quantity=" + quantity + ", colour="
				+ colour + ", itemImages=" + itemImages + ", defaultImage=" + defaultImage + "]";
	}
	
}