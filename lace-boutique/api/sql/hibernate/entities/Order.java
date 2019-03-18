package api.sql.hibernate.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "account_id")
	private Account account;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "order_details_id")
	private OrderDetails orderDetails;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "address_id")
	private Address address;
	
	@Column(name = "order_total")
	private double orderTotal;
	
//	@OneToOne
//	@JoinColumn(name = "order_status_id")
	@Column(name = "order_status_id")
	private int orderStatusId;
	
	@Column(name = "order_type")
	private String orderType;
	
	@Column(name = "delivery_method")
	private String deliveryMethod;
	
	@Column(name = "payment_token")
	private String paymentToken;

	@Column(name = "charge_id")
	private String chargeId;

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

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public double getOrderTotal()
	{
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal)
	{
		this.orderTotal = orderTotal;
	}

	public int getOrderStatusId()
	{
		return orderStatusId;
	}

	public void setOrderStatusId(int orderStatusId)
	{
		this.orderStatusId = orderStatusId;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public String getDeliveryMethod()
	{
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}

	public String getPaymentToken()
	{
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken)
	{
		this.paymentToken = paymentToken;
	}

	public String getChargeId()
	{
		return chargeId;
	}

	public void setChargeId(String chargeId)
	{
		this.chargeId = chargeId;
	}

	public OrderDetails getOrderDetails()
	{
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails)
	{
		this.orderDetails = orderDetails;
	}

	@Override
	public String toString()
	{
		return "Order [id=" + id + ", account=" + account + ", orderDetails=" + orderDetails + ", address=" + address + ", orderTotal=" + orderTotal
			+ ", orderStatusId=" + orderStatusId + ", orderType=" + orderType + ", deliveryMethod=" + deliveryMethod + ", paymentToken=" + paymentToken
			+ ", chargeId=" + chargeId + "]";
	}
	
}
