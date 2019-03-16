package api.sql.hibernate.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wishlist")
public class Wishlist {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="wishlist")
	private List<WishlistItem> wishlistItems;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public List<WishlistItem> getWishlistItem(){
		return this.wishlistItems;
	}
	
	public void setWishlistItemList(ArrayList<WishlistItem> wishlistItemList) {
		this.wishlistItems = wishlistItemList;
	}

	@Override
	public String toString() {
		return "Wishlist [id=" + id + ", account=" + account + ", wishlistItems=[]" +"]";
//		return "Wishlist [id=" + id + ", account=" + account + ", wishlistItems=" + wishlistItems +"]";  <-- MUST NOT DO THIS,  this will kick off a circular join, never ends
	}
	
}
