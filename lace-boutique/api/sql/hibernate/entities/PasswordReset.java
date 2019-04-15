package api.sql.hibernate.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "password_reset")
public class PasswordReset {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "email")
	private String email;

	@Column(name = "code")
	private String code;
	
	@Column(name = "expiry")
	private Timestamp expiry;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Timestamp getExpiry()
	{
		return expiry;
	}

	public void setExpiry(Timestamp expiry)
	{
		this.expiry = expiry;
	}

	@Override
	public String toString()
	{
		return "PasswordReset [id=" + id + ", email=" + email + ", code=" + code + ", expiry=" + expiry + "]";
	}
	
}
