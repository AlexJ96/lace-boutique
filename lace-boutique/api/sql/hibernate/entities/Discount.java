package api.sql.hibernate.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "discount")
public class Discount {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "discount_code")
	private String discountCode;
	
	@Column(name = "min_spend")
	private double minSpend;
	
	@Column(name = "amount_off")
	private double amountOff;
	
	@Column(name = "free_delivery")
	private boolean freeDelivery;
	
	@Column(name = "valid_from")
	@Temporal(TemporalType.DATE)
	private Date validFrom;

	@Column(name = "valid_to")
	@Temporal(TemporalType.DATE)
	private Date validTo;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getDiscountCode()
	{
		return discountCode;
	}

	public void setDiscountCode(String discountCode)
	{
		this.discountCode = discountCode;
	}

	public double getMinSpend()
	{
		return minSpend;
	}

	public void setMinSpend(double minSpend)
	{
		this.minSpend = minSpend;
	}

	public double getAmountOff()
	{
		return amountOff;
	}

	public void setAmountOff(double amountOff)
	{
		this.amountOff = amountOff;
	}

	public boolean isFreeDelivery()
	{
		return freeDelivery;
	}

	public void setFreeDelivery(boolean freeDelivery)
	{
		this.freeDelivery = freeDelivery;
	}

	public Date getValidFrom()
	{
		return validFrom;
	}

	public void setValidFrom(Date validFrom)
	{
		this.validFrom = validFrom;
	}

	public Date getValidTo()
	{
		return validTo;
	}

	public void setValidTo(Date validTo)
	{
		this.validTo = validTo;
	}

	@Override
	public String toString()
	{
		return "Discount [id=" + id + ", discountCode=" + discountCode + ", minSpend=" + minSpend + ", amountOff=" + amountOff + ", freeDelivery="
			+ freeDelivery + ", validFrom=" + validFrom + ", validTo=" + validTo + "]";
	}
	
}
