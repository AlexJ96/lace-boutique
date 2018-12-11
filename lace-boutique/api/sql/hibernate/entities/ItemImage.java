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
@Table(name = "item_image")
public class ItemImage {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@OneToOne
	@JoinColumn(name = "item_id")
	private Item item;
	
	//Seperate to table??
	@Column(name = "colour")
	private String colour;

	@Column(name = "url")
	private String url;

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

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ItemImage [id=" + id + ", item=" + item + ", colour=" + colour + ", url=" + url + "]";
	}

}