package api.sql.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "can_email")
	private Boolean canEmail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getCanEmail() {
		return canEmail;
	}

	public void setCanEmail(Boolean canEmail) {
		this.canEmail = canEmail;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", canEmail=" + canEmail + "]";
	}

}
