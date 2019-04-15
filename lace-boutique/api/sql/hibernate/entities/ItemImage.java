package api.sql.hibernate.entities;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "item_image")
@Embeddable
public class ItemImage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@OneToOne
	@JoinColumn(name = "item_id")
	@NaturalId
	private Item item;

	@ManyToOne
	@JoinColumn(name = "colour_id")
	@NaturalId
	private Colour colour;

	@Column(name = "url")
	private String url;
	
	@Column(name = "default_image")
	private boolean defaultImage;
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}

	public boolean isDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
	}
	
	@Override
	public String toString() {
		return "ItemImage [id=" + id + ", item=" + item + ", colour=" + colour + ", url=" + url + ", defaultImage="
				+ defaultImage + "]";
	}

}