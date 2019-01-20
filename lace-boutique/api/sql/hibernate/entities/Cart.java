package api.sql.hibernate.entities;

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
@Table(name = "cart")
public class Cart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="cartItem")
	private List<CartItem> cartItems;
	
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
	
	public List<CartItem> getCartItems(){
		return this.cartItems;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", account=" + account + ", CartItems=[]" + "]"; // MUST NOT PRINT CARTITEMS
//		return "Cart [id=" + id + ", account=" + account + "]";
	}
	
}
