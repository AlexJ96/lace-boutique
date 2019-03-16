package api.sql.hibernate.entities;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "address")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@Column(name = "number_street")
	private String numberStreet;
	
	@Column(name = "town")
	private String town;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "postcode")
	private String postcode;

	@Column(name = "default_address")
	private boolean defaultAddress;

	public String getNumberStreet()
	{
		return numberStreet;
	}

	public void setNumberStreet(String numberStreet)
	{
		this.numberStreet = numberStreet;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public String getTown()
	{
		return town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public boolean isDefaultAddress()
	{
		return defaultAddress;
	}

	public void setDefaultAddress(boolean defaultAddress)
	{
		this.defaultAddress = defaultAddress;
	}

	@Override
	public String toString()
	{
		return "Address [id=" + id + ", account=" + account + ", numberStreet=" + numberStreet + ", town=" + town + ", city=" + city + ", country=" + country
			+ ", postcode=" + postcode + ", defaultAddress=" + defaultAddress + "]";
	}
	
}
