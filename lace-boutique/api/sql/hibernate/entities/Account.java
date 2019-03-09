package api.sql.hibernate.entities;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "account")
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "title")
	private String title;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_address")
	private String emailAddress;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;

	@Column(name = "password")
	private String password;
	
	@Column(name = "sales_info")
	private String salesInfo;

	@Column(name = "new_stuff_info")
	private String newStuffInfo;

	@Column(name = "news_letter")
	private boolean newsletter;
	
	@Column(name = "created_on")
	private Timestamp createdOn;

	@Column(name = "created_from")
	private String createdFrom;
	
	@Column(name = "last_logged")
	private Timestamp lastLogged;
	
	@Column(name = "logged_from")
	private String loggedFrom;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalesInfo() {
		return salesInfo;
	}

	public void setSalesInfo(String salesInfo) {
		this.salesInfo = salesInfo;
	}

	public String getNewStuffInfo() {
		return newStuffInfo;
	}

	public void setNewStuffInfo(String newStuffInfo) {
		this.newStuffInfo = newStuffInfo;
	}

	public boolean isNewsletter() {
		return newsletter;
	}

	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedFrom() {
		return createdFrom;
	}

	public void setCreatedFrom(String createdFrom) {
		this.createdFrom = createdFrom;
	}

	public Timestamp getLastLogged() {
		return lastLogged;
	}

	public void setLastLogged(Timestamp lastLogged) {
		this.lastLogged = lastLogged;
	}

	public String getLoggedFrom() {
		return loggedFrom;
	}

	public void setLoggedFrom(String loggedFrom) {
		this.loggedFrom = loggedFrom;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth
				+ ", password=" + password + ", salesInfo=" + salesInfo + ", newStuffInfo=" + newStuffInfo
				+ ", newsletter=" + newsletter + ", createdOn=" + createdOn + ", createdFrom=" + createdFrom
				+ ", lastLogged=" + lastLogged + ", loggedFrom=" + loggedFrom + "]";
	}
	
}
